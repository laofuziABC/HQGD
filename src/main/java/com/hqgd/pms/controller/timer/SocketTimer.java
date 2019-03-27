package com.hqgd.pms.controller.timer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.SpringContextUtil;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.tcp.MultiThreadSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketTimer extends TimerTask {
	private EquipmentInfoMapper equipmentInfoMapper;

	public SocketTimer() {
		this.equipmentInfoMapper = SpringContextUtil.getBean(EquipmentInfoMapper.class);
	}

	@Override
	public void run() {
		try {
			doExcute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doExcute() throws IOException, InterruptedException {
		// 拿到所有客户端信息
		List<Socket> socketlist = MultiThreadSocketServer.CLIENT_SOCKET_LIST;
		log.info("socketlist的size=" + socketlist.size());
		List<EquipmentInfo> equipmentList = equipmentInfoMapper.selectAllType1("1");
		log.info("测温仪的size=" + equipmentList.size());
		List<String> frameList = new ArrayList<String>();
		for (EquipmentInfo e : equipmentList) {
			int f = Integer.valueOf(e.getFrameStru());
			int n = e.getNumOfCh();
			String fHex = Integer.toHexString(f);
			String nHex = Integer.toHexString(n * 2);
			if (fHex.length() < 2) {
				fHex = "0" + fHex;
			}
			if (nHex.length() < 2) {
				nHex = "0" + nHex;
			}
			String frame = fHex + " 03 01 48 00 " + nHex;
			String rameStru = CommonUtil.getCRC(frame);
			frameList.add(frame + " " + rameStru);
		}

		for (Socket socket : socketlist) {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			for (String s : frameList) {
				// 循环获取信息
				byte[] bytes = CommonUtil.hexStringToByteArray(s);
				log.info(s);
				dos.write(bytes);
				dos.flush();
				Thread.sleep(500);
			}
		}
	}

	

}
