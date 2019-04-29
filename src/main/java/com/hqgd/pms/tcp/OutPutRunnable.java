package com.hqgd.pms.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import com.hqgd.pms.common.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutPutRunnable implements Runnable {
	private Socket socket;
	private List<String> frameList;

	public OutPutRunnable(Socket socket, List<String> frameList) {
		this.socket = socket;
		this.frameList = frameList;
	}

	@Override
	public void run() {
		OutputStream os = null;
		try {
			os = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < frameList.size(); i++) {
			synchronized (this) {
				String s = frameList.get(i);
				// 循环获取信息
				byte[] bytes = CommonUtil.hexStringToByteArray(s);
				log.info(s);
				try {
					os.write(bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
