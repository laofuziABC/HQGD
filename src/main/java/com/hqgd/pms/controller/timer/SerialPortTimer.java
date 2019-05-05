package com.hqgd.pms.controller.timer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.SpringContextUtil;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.serialPort.SerialPortService;

import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SerialPortTimer extends TimerTask {
	private EquipmentInfoMapper equipmentInfoMapper;

	public SerialPortTimer() {
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
		SerialPort serialPort = SerialPortService.mSerialport;
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			for (int i = 0; i < frameList.size(); i++) {
				synchronized (this) {
					String s = frameList.get(i);
					// 循环获取信息
					byte[] bytes = CommonUtil.hexStringToByteArray(s);
					log.info(s);
					out.write(bytes);
					out.flush();
					Thread.sleep(1500);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
