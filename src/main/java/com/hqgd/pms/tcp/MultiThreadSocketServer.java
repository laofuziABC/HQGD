package com.hqgd.pms.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *  描述：socket服务端监听程序
 * @date: 2019年2月1日 上午9:54:06
 * @author: yaorong 
 * @ClassName: MultiThreadSocketServer 
 * </pre>
 */
public class MultiThreadSocketServer implements Runnable {

	private static final int SERVER_PORT = 12345;

	public static List<Socket> CLIENT_SOCKET_LIST = new ArrayList<Socket>();

	@Override
	public void run() {
		System.out.println("MultiThreadSocketServer run Start...");
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("MultiThreadSocketServer started, server_port：" + SERVER_PORT);
			// 启动socket服务监听
			while (true) {
				// 等待接收客户端连接
				socket = serverSocket.accept();
				System.out.println("client join in, ip:" + socket.getInetAddress());
				CLIENT_SOCKET_LIST.add(socket);
				// 将接收到的客户端socket交给处理线程进行处理，实现多线程
				new Thread(new ClientSocketHandler(socket)).start();
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
