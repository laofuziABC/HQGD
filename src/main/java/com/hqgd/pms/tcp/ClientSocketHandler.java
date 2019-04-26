package com.hqgd.pms.tcp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.RouterInfoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;
import com.hqgd.pms.service.equipment.impl.EquipmentService;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *  描述：服务端处理客户端信息的线程
 * &#64;date: 2019年2月 1日 上午10:48:42
 * &#64;author: yaorong 
 * &#64;ClassName: ClientSocketHandler
 * </pre>
 */
@Slf4j
public class ClientSocketHandler implements Runnable {

	/**
	 * 前置机接收到的端服务器socket信息
	 */
	private Socket socket;
	private EquipmentService equipmentService;
	private DataAcquisitionVoMapper dam;
	private IDataAcquisitionService das;
	private SimpMessagingTemplate simpMessage;
	private RouterInfoMapper routerInfoMapper;

	public ClientSocketHandler(Socket socket, EquipmentService equipmentService, DataAcquisitionVoMapper dam,
			IDataAcquisitionService das, SimpMessagingTemplate simpMessage, RouterInfoMapper routerInfoMapper) {
		this.socket = socket;
		this.equipmentService = equipmentService;
		this.dam = dam;
		this.das = das;
		this.simpMessage = simpMessage;
		this.routerInfoMapper = routerInfoMapper;
	}

	DataInputStream socketIn = null;
	PrintStream socketOut = null;
	String heartbeat = "ht1";

	@Override
	public void run() {
		try {
			socketIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			// 获取设备发送的数据
			byte[] bytes = new byte[1]; // 一次读取一个byte
			String ret = "";
			String inputString = "";

			while (socketIn.read(bytes) > 0) {
				ret += bytesToHexString(bytes);
				if (socketIn.available() == 0) { // 一个请求
					inputString = ret;
					ret = "";
					String frameStru = "";
					// 获取心跳包id,判断数据长度，当最少只有一个通道的时候，数据为“0103040121CDB6",长度为14，而14已经亿亿，不会有那么多id编号
					// if (count < 2) {
					// heartbeat = inputString.substring(0, 30);
					// heartbeat = hexStr2Str(heartbeat);
					// log.info(Thread.currentThread().getName() + " say :" + heartbeat);
					// List<Map<String, String>> el = equipmentService.selectAllByHb(heartbeat);
					// Map<String, String> resultMap = new HashMap<String, String>();
					// String ip = String.valueOf(socket.getInetAddress()).substring(1);
					// resultMap.put("id", heartbeat);
					// resultMap.put("text", ip);
					// resultMap.put("parent", "#");
					// el.add(resultMap);
					// simpMessage.convertAndSend("/topic/ip", "该路由下的设备有" + el);
					// routerInfoMapper.updateIp(heartbeat, ip);
					// count++;
					// } else if (len > 21 && count > 1) {
					simpMessage.convertAndSend("/topic/ip", "inputString" + inputString);
					List<DataAcquisitionVo> realTimeDateList = new ArrayList<DataAcquisitionVo>();
					log.info(Thread.currentThread().getName() + " say :inputString=" + inputString);
					// 开始校验客户端发送过来的数据
					// 获取返回祯的CRC校验
					String rameStru = inputString.substring(inputString.length() - 4);
					// 获取返回祯的有效数据（即除去CRC校验的剩余祯）
					String validInput = inputString.substring(0, inputString.length() - 4);
					// 对有效数据进行CRC校验码验证
					String crc = CommonUtil.getCRC(validInput).replace(" ", "").toLowerCase();
					// 判断返回祯的CRC校验码是否为真
					if (crc.equals(rameStru)) {
						Map<String, String> param = new HashMap<>();
						frameStru = Integer.valueOf(inputString.substring(0, 2), 16).toString();
						// 通道数量
						int num = Integer.valueOf(inputString.substring(4, 6), 16) / 4;
						log.info("客户端返回数据获取的通道数量为：" + num + "----祯是：" + frameStru);
						// 去掉前六个标识性字符，真正的有效字符是从第七个开始
						inputString = inputString.substring(6);
						param.put("frameStru", frameStru);
						param.put("heartbeatId", heartbeat);
						// 通过心跳包ID和设备的帧结构获取唯一设备信息
						EquipmentInfo e = equipmentService.selectByHbid(param);
						if (e != null) {
							String equipmentId = e.getEquipmentId();
							String equipmentName = e.getEquipmentName();
							String channelTem = e.getChannelTem();
							channelTem = channelTem.substring(2, channelTem.length() - 2);
							String[] arr = channelTem.split("\\],\\[");
							List<String> cnl = new ArrayList<String>();// 通道list
							List<String> opticall = new ArrayList<String>();// 光纤位置list
							List<String> maxl = new ArrayList<String>();// 最大值list
							List<String> minl = new ArrayList<String>();// 最小值list
							if (arr.length == e.getNumOfCh()) {
								for (int i = 0; i < e.getNumOfCh(); i++) {
									String[] ta = arr[i].split(",");
									String cn = ta[0].substring(1, ta[0].length() - 1);
									String optical = ta[1].substring(1, ta[1].length() - 1);
									String max = ta[2].substring(1, ta[2].length() - 1);
									String min = ta[3].substring(1, ta[3].length() - 1);
									cnl.add(cn);
									opticall.add(optical);
									maxl.add(max);
									minl.add(min);
								}
							}
							// 测温仪的公共信息
							String receiveTime = CommonUtil.getSimpleFormatTimestamp();
							// 测温仪 每个通道的信息
							for (int i = 0; i < num; i++) {
								DataAcquisitionVo d = new DataAcquisitionVo();
								d.setEquipmentId(equipmentId);
								d.setEquipmentName(equipmentName);
								d.setAddress(frameStru);
								d.setReceiveTime(receiveTime);
								d.setDutyPerson(e.getUserName());
								d.setTel(e.getTel());
								d.setNumOfCh(e.getNumOfCh());
								Float temp = Integer.valueOf(inputString.substring(i * 8, i * 8 + 4), 16) / 10F;
								Integer pd = Integer.valueOf(inputString.substring(i * 8 + 4, i * 8 + 6), 16);
								Integer uv = Integer.valueOf(inputString.substring(i * 8 + 6, i * 8 + 8), 16);
								d.setChannelNum(cnl.get(i));
								d.setOpticalFiberPosition(opticall.get(i));
								d.setTemperature(temp.toString());
								d.setPd(pd.toString());
								d.setUv(uv.toString());
								switch (temp.toString()) {
								case "3000.0":
									d.setState(2);
									d.setTemperature("3000");
									d.setMessage("光纤故障");
									break;
								case "6116.6":
									d.setState(1);
									d.setTemperature("-437");
									d.setMessage("传感器模块故障");
									break;
								case "2999.9":
									d.setState(3);
									d.setTemperature("2999");
									d.setMessage("系统调整中");
									break;
								default:
									d.setState(5);
									d.setMessage("正常");
									break;
								}
								if (temp != 3000.0F && temp != -6116.6F && temp != 2999.9F && temp != 6548.3F
										&& (temp < Float.valueOf(minl.get(i)) || temp > Float.valueOf(maxl.get(i)))) {
									d.setState(9);
								}
								// 往SQL server中插入数据，为了找问题方便看
								dam.insertSQL(d);
								// 往Hbase中插入数据
								das.insertHbase(d);
								realTimeDateList.add(d);
							}
							// 往前台推送
							simpMessage.convertAndSend("/topic/real", realTimeDateList);
							log.info("realTimeDateList的size----" + realTimeDateList.size());
						}
					}
				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socketIn != null) {
					socketIn.close();
				}
				if (socketOut != null) {
					socketOut.close();
				}
				if (socket != null) {
					String ip = socket.getInetAddress().toString().replaceAll("/", "");
					simpMessage.convertAndSend("/topic/ip", ip + "断开连接！！！");
					routerInfoMapper.updateConnect(heartbeat);
					MultiThreadSocketServer.IP_SET.remove(ip);
					MultiThreadSocketServer.CLIENT_SOCKET_LIST.remove(socket);
					socket.shutdownOutput();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
