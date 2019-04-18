package com.hqgd.pms.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hqgd.pms.common.SpringContextUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.RouterInfoMapper;
import com.hqgd.pms.dao.system.SysParamMapper;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;
import com.hqgd.pms.service.equipment.impl.EquipmentService;

/**
 * <pre>
 *  描述：socket服务端监听程序
 * &#64;date: 2019年2月1日 上午9:54:06
 * &#64;author: yaorong 
 * &#64;ClassName: MultiThreadSocketServer
 * </pre>
 */
@CacheConfig(cacheNames = "port")
public class MultiThreadSocketServer implements Runnable {

	private int SERVER_PORT = 12345;
	private EquipmentService equipmentService;
	private RouterInfoMapper routerInfoMapper;
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	private IDataAcquisitionService das;
	private SimpMessagingTemplate simpMessage;// 消息发送模板
	private SysParamMapper sysParamMapper;// 消息发送模板
	public static List<Socket> CLIENT_SOCKET_LIST = new ArrayList<Socket>();
	public static HashSet<String> IP_SET = new HashSet<String>();

	public MultiThreadSocketServer() {
		this.equipmentService = SpringContextUtil.getBean(EquipmentService.class);
		this.dataAcquisitionVoMapper = SpringContextUtil.getBean(DataAcquisitionVoMapper.class);
		this.das = SpringContextUtil.getBean(IDataAcquisitionService.class);
		this.simpMessage = SpringContextUtil.getBean(SimpMessagingTemplate.class);
		this.routerInfoMapper = SpringContextUtil.getBean(RouterInfoMapper.class);
		this.sysParamMapper = SpringContextUtil.getBean(SysParamMapper.class);
	}

	@Cacheable
	public void selectPort() {
		int port = Integer.valueOf(sysParamMapper.selectByPrimaryKey("SERVER_PORT").getParamValue());
		SERVER_PORT = port;
	}

	@Override
	public void run() {
		System.out.println("MultiThreadSocketServer run Start...");
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			selectPort();
			serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("MultiThreadSocketServer started, server_port：" + SERVER_PORT);
			// 启动socket服务监听
			while (true) {
				// 等待接收客户端连接
				socket = serverSocket.accept();
				System.out.println("client join in, ip:" + socket.getInetAddress());
				simpMessage.convertAndSend("/topic/ip", socket.getInetAddress() + "建立连接！！！！");
				String ip = socket.getInetAddress().toString().replaceAll("/", "");
				if (!IP_SET.contains(ip)) {
					CLIENT_SOCKET_LIST.add(socket);
					// 将接收到的客户端socket交给处理线程进行处理，实现多线程
					new Thread(new ClientSocketHandler(socket, equipmentService, dataAcquisitionVoMapper, das,
							simpMessage, routerInfoMapper)).start();
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
