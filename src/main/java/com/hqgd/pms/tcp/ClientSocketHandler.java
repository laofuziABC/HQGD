package com.hqgd.pms.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.hqgd.pms.service.equipment.impl.EquipmentService;

/**
 * <pre>
 *  描述：服务端处理客户端信息的线程
 * &#64;date: 2019年2月 1日 上午10:48:42
 * &#64;author: yaorong 
 * &#64;ClassName: ClientSocketHandler
 * </pre>
 */
public class ClientSocketHandler implements Runnable {

	/**
	 * 前置机接收到的端服务器socket信息
	 */
	private Socket socket;
	private EquipmentService equipmentService;
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	private SimpMessagingTemplate simpMessage;
	private RouterInfoMapper routerInfoMapper;

	public ClientSocketHandler(Socket socket, EquipmentService equipmentService,
			DataAcquisitionVoMapper dataAcquisitionVoMapper, SimpMessagingTemplate simpMessage,
			RouterInfoMapper routerInfoMapper) {
		this.socket = socket;
		this.equipmentService = equipmentService;
		this.dataAcquisitionVoMapper = dataAcquisitionVoMapper;
		this.simpMessage = simpMessage;
		this.routerInfoMapper = routerInfoMapper;
	}

	BufferedReader socketIn = null;
	PrintStream socketOut = null;
	String heartbeat = "";

	@Override
	public void run() {
		try {
			socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socketOut = new PrintStream(socket.getOutputStream());
			while (true) {
				// 获取设备发送的数据
				String inputString = socketIn.readLine();
				System.out.println(Thread.currentThread().getName() + " say :" + inputString);
				inputString = inputString.replace(" ", "").trim();
				int len = inputString.length();
				String frameStru = "";
				int count = 1;
				// 获取心跳包id,判断数据长度，当最少只有一个通道的时候，数据为“0103040121CDB6",长度为14，而14已经亿亿，不会有那么多id编号
				if (len < 16 && count < 2) {
					heartbeat = inputString;
					List<Map<String, String>> el = equipmentService.selectAllByHb(heartbeat);
					Map<String, String> resultMap = new HashMap<String, String>();
					String ip = String.valueOf(socket.getInetAddress()).substring(1);
					resultMap.put("id", heartbeat);
					resultMap.put("text", ip);
					resultMap.put("parent", "#");
					el.add(resultMap);
					simpMessage.convertAndSend("/topic/ip", "该路由下的设备有" + el);
					routerInfoMapper.updateIp(heartbeat, ip);
					count++;
				} else {
					// 解析客户端发送过来的数据
					// 获取设备地址编码
					Map<String, String> param = new HashMap<>();
					frameStru = Integer.valueOf(inputString.substring(0, 2), 16).toString();
					int num = Integer.valueOf(inputString.substring(4, 6), 16) / 4;
					inputString = inputString.substring(6);
					param.put("frameStru", frameStru);
					param.put("heartbeatId", heartbeat);
					EquipmentInfo e = equipmentService.selectByHbid(param);
					if (e != null) {
						String equipmentId = e.getEquipmentId();
						String equipmentName = e.getEquipmentName();
						String channelTem = e.getChannelTem();
						channelTem = channelTem.substring(2, channelTem.length() - 2);
						String[] arr = channelTem.split("\\],\\[");
						List<String> cnl = new ArrayList<String>();
						List<String> opticall = new ArrayList<String>();
						List<String> maxl = new ArrayList<String>();
						List<String> minl = new ArrayList<String>();
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
						DataAcquisitionVo d = new DataAcquisitionVo();
						d.setEquipmentId(equipmentId);
						d.setEquipmentName(equipmentName);
						d.setAddress(frameStru);
						d.setReceiveTime(CommonUtil.getSimpleFormatTimestamp());
						d.setDutyPerson(e.getUserName());
						d.setTel(e.getTel());
						d.setNumOfCh(e.getNumOfCh());
						String type = e.getType();
						switch (type) {
						case "1":
							dataAcquisitionVoMapper.truncatef1();
							break;
						case "2":
							dataAcquisitionVoMapper.truncatef2();
							break;
						case "3":
							dataAcquisitionVoMapper.truncatef3();
							break;
						case "4":
							dataAcquisitionVoMapper.truncatef4();
							break;
						}
						for (int i = 0; i < num; i++) {
							Float value = Integer.valueOf(inputString.substring(i * 4, i * 4 + 4), 16) / 10F;
							Float pd = Integer.valueOf(inputString.substring(i * 4 + 4, i * 4 + 6), 16) / 10F;
							Float uv = Integer.valueOf(inputString.substring(i * 4 + 6, i * 4 + 8), 16) / 10F;
							d.setChannelNum(cnl.get(i));
							d.setOpticalFiberPosition(opticall.get(i));
							d.setTemperature(value.toString());
							d.setPd(pd.toString());
							d.setUv(uv.toString());
							switch (value.toString()) {
							case "3000.0":
								d.setState("2");
								d.setMessage("光纤故障");
								break;
							case "6116.6":
								d.setState("1");
								d.setTemperature("-437");
								d.setMessage("传感器模块故障");
								break;
							case "2999.9":
								d.setState("3");
								d.setMessage("系统调整中");
								break;
							default:
								d.setState("5");
								d.setMessage("正常");
								break;
							}
							if (value != 3000.0 && value != 6116.6 && value != 2999.9
									&& (value < Float.valueOf(minl.get(i)) || value > Float.valueOf(maxl.get(i)))) {
								d.setState("9");
							}

							switch (type) {
							case "1":
								dataAcquisitionVoMapper.insert1(d);
								dataAcquisitionVoMapper.insertf1(d);
								break;
							case "2":
								dataAcquisitionVoMapper.insert2(d);
								dataAcquisitionVoMapper.insertf2(d);
								break;
							case "3":
								dataAcquisitionVoMapper.insert3(d);
								dataAcquisitionVoMapper.insertf3(d);
								break;
							case "4":
								dataAcquisitionVoMapper.insert4(d);
								dataAcquisitionVoMapper.insertf4(d);
								break;
							}
						}
					}

				}
			}
		} catch (Exception e) {
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
					simpMessage.convertAndSend("/topic/ip", socket.getInetAddress() + "断开连接！！！");
					routerInfoMapper.updateConnect(heartbeat);
					MultiThreadSocketServer.CLIENT_SOCKET_LIST.remove(socket);
					socket.shutdownOutput();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
