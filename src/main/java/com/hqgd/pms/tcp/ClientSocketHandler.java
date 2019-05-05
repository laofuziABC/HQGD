package com.hqgd.pms.tcp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.RouterInfoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
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
	private IDataAcquisitionService das;
	private SimpMessagingTemplate simpMessage;
	private RouterInfoMapper routerInfoMapper;

	public ClientSocketHandler(Socket socket, EquipmentService equipmentService, DataAcquisitionVoMapper dam,
			IDataAcquisitionService das, SimpMessagingTemplate simpMessage, RouterInfoMapper routerInfoMapper) {
		this.socket = socket;
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
				ret += CommonUtil.bytesToHexString(bytes);
				if (socketIn.available() == 0) { // 一个请求
					inputString = ret;
					ret = "";
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
					log.info(Thread.currentThread().getName() + " say :inputString=" + inputString);
					// 开始校验客户端发送过来的数据
					List<DataAcquisitionVo> realTimeDateList = das.parseReceivedData(inputString, "socket");
					// 往前台推送
					simpMessage.convertAndSend("/topic/real", realTimeDateList);
					log.info("realTimeDateList的size----" + realTimeDateList.size());
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

}
