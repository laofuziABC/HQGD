package com.hqgd.pms.communicate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hqgd.pms.common.ExcelUtil;
import com.hqgd.pms.domain.EquipmentInfo;

public class Demo1_Server {
	public static void main(String[] args) throws IOException {
		// extracted();
		// extracted1();
		@SuppressWarnings("resource")
		final ServerSocket server = new ServerSocket(54321,20);
		System.out.println("服务器启动，绑定54321端口");
		List<EquipmentInfo> equipmentList = new ArrayList<>();
		equipmentList.addAll(ExcelUtil.readExcel("D:\\PMS\\20181211125835equipmentInfo.xls"));
		while (true) {
			final Socket socket = server.accept();
			new Thread() {
				public void run() {
					try {
						BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						PrintStream ps = new PrintStream(socket.getOutputStream());
						String line = br.readLine();
						if("quit".equals(line)) {
							
							socket.close();
							
						}
						line = new StringBuilder(line).reverse().toString();
						ps.println(line);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
		
	}

	@SuppressWarnings("unused")
	private static void extracted1() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(12345);
		Socket socket = server.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintStream ps = new PrintStream(socket.getOutputStream());
		ps.println("欢迎");
		System.err.println(br.readLine());
	}

	@SuppressWarnings("unused")
	private static void extracted() throws IOException {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(12345);
		Socket socket = server.accept();
		InputStream is = socket.getInputStream();
		OutputStream os = socket.getOutputStream();
		os.write("百度一下你就知道".getBytes());

		byte[] arr = new byte[1024];
		int len = is.read(arr);
		socket.close();
	}
}
