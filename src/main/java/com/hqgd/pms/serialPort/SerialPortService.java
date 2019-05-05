package com.hqgd.pms.serialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.SpringContextUtil;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.SerialPortVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SerialPortService {
	public static SerialPort mSerialport = null;
	private SimpMessagingTemplate simpMessage;
	@Autowired
	private IDataAcquisitionService das;
	static String heartbeat = "ht1";

	public SerialPortService() {
		this.das = SpringContextUtil.getBean(IDataAcquisitionService.class);
		this.simpMessage = SpringContextUtil.getBean(SimpMessagingTemplate.class);
	}

	public Map<String, Object> openSort(SerialPortVo serialPortVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 串口对象
		String commName = serialPortVo.getSerialPort();
		int baudrate = Integer.valueOf(serialPortVo.getBaudrate());
		int dataBits = Integer.valueOf(serialPortVo.getDataBits());
		int parity = Integer.valueOf(serialPortVo.getParity());
		int stopBits = Integer.valueOf(serialPortVo.getStopBits());
		try {
			mSerialport = openPort(commName, baudrate, dataBits, parity, stopBits);
			if (mSerialport != null) {
				resultMap.put("message", "串口已打开");
				resultMap.put("result", true);
			}else {
				resultMap.put("message", "打开串口失败，请检查参数设置是否对应");
				resultMap.put("result", false);
			}
		} catch (PortInUseException e) {
			resultMap.put("message", "串口已被占用！");
			resultMap.put("result", false);
		}

		// 添加串口监听
		addListener(mSerialport, new DataAvailableListener() {
			
			@Override
			public void dataAvailable() {
				try {
					if (mSerialport == null) {
						resultMap.put("message", "串口对象为空，监听失败！");
						resultMap.put("result", false);
					} else {
						// 读取串口数据
						readFromPort(mSerialport);
						// bytes = SerialPortManager.readFromPort(mSerialport);
						// // 以十六进制的形式接收数据
						// data = ByteUtils.byteArrayToHexString(bytes);
						// log.info(data);
						// resultMap.put("data", data);
					}
				} catch (Exception e) {
					// 发生读取错误时显示错误信息后退出系统
					// System.exit(0);
					e.printStackTrace();
				}
			}
		});
		return resultMap;
	}

	public void closePort() {
		if (mSerialport != null) {
			mSerialport.close();
		}
		mSerialport = null;
	}

	/**
	 * 查找所有可用端口
	 * 
	 * @return 可用端口名称列表
	 */
	public ArrayList<String> findPorts() {
		// 获得当前所有可用串口
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> portNameList = new ArrayList<String>();
		// 将可用串口名添加到List并返回该List
		while (portList.hasMoreElements()) {
			String portName = portList.nextElement().getName();
			portNameList.add(portName);
		}
		return portNameList;
	}

	/**
	 * 打开串口
	 * 
	 * @param portName
	 *            端口名称
	 * @param baudrate
	 *            波特率
	 * @param stopBits
	 * @param parity
	 * @param dataBits
	 * @return 串口对象
	 * @throws PortInUseException
	 *             串口已被占用
	 */
	public SerialPort openPort(String portName, int baudrate, int dataBits, int parity, int stopBits)
			throws PortInUseException {
		try {
			// 通过端口名识别端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			// 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
			CommPort commPort = portIdentifier.open(portName, 2000);
			// 判断是不是串口
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				try {
					// 设置一下串口的波特率等参数
					serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
				} catch (UnsupportedCommOperationException e) {
					e.printStackTrace();
				}
				return serialPort;
			}
		} catch (NoSuchPortException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return
	 * @return 读取到的数据
	 */
	public List<DataAcquisitionVo> readFromPort(SerialPort serialPort) {
		InputStream in = null;
		List<DataAcquisitionVo> realTimeDateList = new ArrayList<DataAcquisitionVo>();
		try {
			in = serialPort.getInputStream();
			// 缓冲区大小为一个字节
			// byte[] readBuffer = new byte[1];
			// int bytesNum = in.read(readBuffer);
			// while (bytesNum > 0) {
			// bytes = ArrayUtils.concat(bytes, readBuffer);
			// bytesNum = in.read(readBuffer);
			// }
			byte[] bytes = new byte[1]; // 一次读取一个byte
			String ret = "";
			String inputString = "";

			while (in.read(bytes) > 0) {
				ret += CommonUtil.bytesToHexString(bytes);
				if (in.available() == 0) { // 一个请求
					inputString = ret;
					ret = "";
					log.info(Thread.currentThread().getName() + " say :inputString=" + inputString);
					// 开始校验客户端发送过来的数据
					realTimeDateList = das.parseReceivedData(inputString, "serialPort");
					// 往前台推送
					simpMessage.convertAndSend("/topic/serialPort", realTimeDateList);
					log.info("realTimeDateList的size----" + realTimeDateList.size());

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return realTimeDateList;

	}

	/**
	 * 添加监听器
	 * 
	 * @param port
	 *            串口对象
	 * @param listener
	 *            串口存在有效数据监听
	 */
	public void addListener(SerialPort serialPort, DataAvailableListener listener) {
		try {
			// 给串口添加监听器
			serialPort.addEventListener(new SerialPortListener(listener));
			// 设置当有数据到达时唤醒监听接收线程
			serialPort.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			serialPort.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 串口监听
	 */
	public class SerialPortListener implements SerialPortEventListener {

		private DataAvailableListener mDataAvailableListener;

		public SerialPortListener(DataAvailableListener mDataAvailableListener) {
			this.mDataAvailableListener = mDataAvailableListener;
		}

		public void serialEvent(SerialPortEvent serialPortEvent) {
			switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
				if (mDataAvailableListener != null) {
					mDataAvailableListener.dataAvailable();
				}
				break;

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
				break;

			case SerialPortEvent.CTS: // 3.清除待发送数据
				break;

			case SerialPortEvent.DSR: // 4.待发送数据准备好了
				break;

			case SerialPortEvent.RI: // 5.振铃指示
				break;

			case SerialPortEvent.CD: // 6.载波检测
				break;

			case SerialPortEvent.OE: // 7.溢位（溢出）错误
				break;

			case SerialPortEvent.PE: // 8.奇偶校验错误
				break;

			case SerialPortEvent.FE: // 9.帧错误
				break;

			case SerialPortEvent.BI: // 10.通讯中断
				ShowUtils.errorMessage("与串口设备通讯中断");
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 串口存在有效数据监听
	 */
	public interface DataAvailableListener {
		/**
		 * 串口存在有效数据
		 */
		void dataAvailable();
	}

}
