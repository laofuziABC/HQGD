package com.hqgd.pms.service.serialPort;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hqgd.pms.domain.SerialPortVo;
import com.hqgd.pms.service.serialPort.SerialPortManager;
import com.hqgd.pms.service.serialPort.ByteUtils;

import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SerialPortService {
	SerialPort mSerialport = null;

	public Map<String, Object> openSort(SerialPortVo serialPortVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 串口对象

		String commName = serialPortVo.getSerialPort();
		int baudrate = serialPortVo.getBaudrate();
		int dataBits = serialPortVo.getDataBits();
		int parity = serialPortVo.getParity();
		int stopBits = serialPortVo.getStopBits();
		try {
			mSerialport = SerialPortManager.openPort(commName, baudrate, dataBits, parity, stopBits);
			if (mSerialport != null) {
				resultMap.put("message", "串口已打开");
			}
		} catch (PortInUseException e) {
			resultMap.put("message", "串口已被占用！");
		}

		// 添加串口监听
		SerialPortManager.addListener(mSerialport, new SerialPortManager.DataAvailableListener() {

			@Override
			public void dataAvailable() {
				String data = "";
				byte[] bytes = null;
				try {
					if (mSerialport == null) {
						resultMap.put("message", "串口对象为空，监听失败！");
					} else {
						// 读取串口数据
						bytes = SerialPortManager.readFromPort(mSerialport);
						// 以十六进制的形式接收数据
						data = ByteUtils.byteArrayToHexString(bytes);
						log.info(data);
					}
				} catch (Exception e) {

					// 发生读取错误时显示错误信息后退出系统
					System.exit(0);
				}
			}
		});
		return resultMap;
	}

	public void closePort() {
		SerialPortManager.closePort(mSerialport);
		mSerialport = null;
	}

}
