package com.hqgd.pms.communicate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hqgd.pms.common.ExcelUtil;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.equipment.IEquipmentService;

public class Server {
	@Autowired
	IEquipmentService equipmentService;
	private final static String EXCEL_FILE_PATH_EQUIPMENT_FRAMESTRU = "D:\\PMS\\20181211125835equipmentInfo.xls";
	private static BufferedReader br;
	public static HashMap<String, Socket> socketList = new HashMap<>();
	public static String channelToken;
	public static void main(String[] args) throws IOException {
		List<EquipmentInfo> equipmentList = new ArrayList<>();
		equipmentList.addAll(ExcelUtil.readExcel(EXCEL_FILE_PATH_EQUIPMENT_FRAMESTRU));
		List<Socket> list = new ArrayList<>();
		@SuppressWarnings("resource")
		final ServerSocket server = new ServerSocket(54321);
		System.out.println("服务器启动，绑定54321端口");
		while (true) {
			final Socket socket = server.accept();
			list.add(socket);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            channelToken = br.readLine();
            socketList.put(channelToken,socket);
            
			new Thread() {
				public void run() {
					Socket socket;
					System.out.println("Client: "+getName()+" come in...");

					//每当客户端连接上,就向相应的客户端进行回应
					Iterator<HashMap.Entry<String, Socket>> entries = socketList.entrySet().iterator(); 
					while (entries.hasNext()){
					    HashMap.Entry<String, Socket> entry = entries.next(); 
					    System.out.println(entry.getKey());
					    if (!String.valueOf(entry.getKey()).equals("")) {
					        System.out.println(entry.getValue());
					        System.out.println("-------------");
					        socket = entry.getValue();
					        if (socket!=null) {
					            try {
					            	PrintWriter out = new PrintWriter(socket.getOutputStream());  //回复client的ID
					                out.println(entry.getKey());
					                out.flush();
					            } catch (IOException e) {
					                e.printStackTrace();
					            }
					        }
					    }
					}
				}
			}.start();
		}
	}

}
