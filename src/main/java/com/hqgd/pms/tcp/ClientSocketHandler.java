package com.hqgd.pms.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * <pre>
 *  描述：服务端处理客户端信息的线程
 * @date: 2019年2月 1日 上午10:48:42
 * @author: yaorong 
 * @ClassName: ClientSocketHandler 
 * </pre>
 */
public class ClientSocketHandler implements Runnable {

	/**
	 * 前置机接收到的端服务器socket信息
	 */
	private Socket socket;

	public ClientSocketHandler(Socket socket) {
		this.socket = socket;
	}

	private BufferedInputStream socketIn = null;
	private BufferedOutputStream socketOut = null;

	@Override
	public void run() {
		try {
			socketIn = new BufferedInputStream(socket.getInputStream());
			socketOut = new BufferedOutputStream(socket.getOutputStream());
			while (true) {
				byte[] lengthArray = new byte[4];
				socketIn.read(lengthArray, 0, 4);// 使用阻塞方法来等待数据的返回(读取存储入参长度的4字节)
				int len = 0;// 存储接收到的参数长度
				// 解析入参的长度
				for (int i = 0; i < lengthArray.length; i++) {
					len += ((int) lengthArray[i] & 0xff) << (lengthArray.length - 1 - i) * 8;
				}
				byte[] byteArray = new byte[len]; // 接收入参的byte数组
				socketIn.read(byteArray, 0, len);// 使用阻塞方法来等待数据的返回(按照长度读取入参的值)
				// 解析入参
				String inputString = new String(byteArray, 0, len);
				System.out.println(Thread.currentThread().getName() + " say :" + inputString);
				// 返回结果
				String resultString = "";
				if (inputString.lastIndexOf("I'm coming") > -1) {
					resultString = "Hello , welcome to join";
					sendMessage(socketOut, resultString);
				}
				//sendMessage(socketOut, resultString);
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
					socket.shutdownOutput();
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void sendMessage(BufferedOutputStream socketOut, String message) throws IOException {
		if (message != null && message.trim().length() > 0 && socketOut != null) {
			// 发送长度信息
			socketOut.write(getStringLength(message));
			// 发送参数字符串
			socketOut.write(message.getBytes());
			socketOut.flush();
		}
	}

	/**
	 * <pre>
	 * 描述：用来生成4字节的byte[]，存储发送字符串的长度
	 * 作者：yaorong 
	 * 时间：2019年2月1日 16:09:31
	 * @param sourceString
	 * @return
	 * returnType：byte[]
	 * </pre>
	 */
	private static byte[] getStringLength(String sourceString) {
		int sourceLength = sourceString.getBytes().length;
		byte[] lengthArray = new byte[4];
		for (int i = 0; i < lengthArray.length; i++) {
			lengthArray[i] = (byte) ((sourceLength >> (lengthArray.length - 1 - i) * 8) & 0xff);
		}
		return lengthArray;
	}

}
