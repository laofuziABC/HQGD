package com.hqgd.pms.serialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;
import com.hqgd.pms.service.equipment.impl.EquipmentService;
import com.hqgd.pms.tcp.ClientSocketHandler;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 串口管理
 * 
 * @author yangle
 */
@Slf4j
@SuppressWarnings("all")
public class SerialPortManager {
	private static SimpMessagingTemplate simpMessage;
	@Autowired
	private static EquipmentService equipmentService;
	@Autowired
	private static DataAcquisitionVoMapper dam;
	@Autowired
	private static IDataAcquisitionService das;
	static String heartbeat = "ht1";

	/**
	 * 查找所有可用端口
	 * 
	 * @return 可用端口名称列表
	 */
	public static final ArrayList<String> findPorts() {
		// 获得当前所有可用串口
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
	public static final SerialPort openPort(String portName, int baudrate, int dataBits, int parity, int stopBits)
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
	 * 关闭串口
	 * 
	 * @param serialport
	 *            待关闭的串口对象
	 */
	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
		}
	}

	/**
	 * 往串口发送数据
	 * 
	 * @param serialPort
	 *            串口对象
	 * @param order
	 *            待发送数据
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order) {
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
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

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 */
	public static void readFromPort(SerialPort serialPort) {
		InputStream in = null;
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
					String frameStru = "";
					List<DataAcquisitionVo> realTimeDateList = new ArrayList<DataAcquisitionVo>();
					log.info(Thread.currentThread().getName() + " say :inputString=" + inputString);
					// 开始校验客户端发送过来的数据
					// 获取返回祯的CRC校验
					String rameStru = inputString.substring(inputString.length() - 4);
					// 获取返回祯的有效数据（即除去CRC校验的剩余祯）
					String validInput = inputString.substring(0, inputString.length() - 4);
					// 对有效数据进行CRC校验码验证
					String crc = CommonUtil.getCRC(validInput).replace(" ", "").toLowerCase();
					// 判断返回祯的CRC校验码是否为真
					if (crc.equals(rameStru)) {
						Map<String, String> param = new HashMap<>();
						frameStru = Integer.valueOf(inputString.substring(0, 2), 16).toString();
						// 通道数量
						int num = Integer.valueOf(inputString.substring(4, 6), 16) / 4;
						log.info("客户端返回数据获取的通道数量为：" + num + "----祯是：" + frameStru);
						// 去掉前六个标识性字符，真正的有效字符是从第七个开始
						inputString = inputString.substring(6);
						param.put("frameStru", frameStru);

						param.put("heartbeatId", heartbeat);
						// 通过心跳包ID和设备的帧结构获取唯一设备信息
						EquipmentInfo e = equipmentService.selectByHbid(param);
						if (e != null) {
							String equipmentId = e.getEquipmentId();
							String equipmentName = e.getEquipmentName();
							String channelTem = e.getChannelTem();
							channelTem = channelTem.substring(2, channelTem.length() - 2);
							String[] arr = channelTem.split("\\],\\[");
							List<String> cnl = new ArrayList<String>();// 通道list
							List<String> opticall = new ArrayList<String>();// 光纤位置list
							List<String> maxl = new ArrayList<String>();// 最大值list
							List<String> minl = new ArrayList<String>();// 最小值list
							if (arr.length == e.getNumOfCh()) {
								for (int i = 0; i < e.getNumOfCh(); i++) {
									String[] ta = arr[i].split(",");
									String cn = ta[0].substring(1, ta[0].length() - 1);
									String optical = ta[1].substring(1, ta[1].length() - 1);
									String max = ta[2].substring(1, ta[2].length() - 1);
									String min = ta[3].substring(1, ta[3].length() - 1);
									cnl.add(cn);
									opticall.add(optical);
									maxl.add(max);
									minl.add(min);
								}
							}
							// 测温仪的公共信息
							String receiveTime = CommonUtil.getSimpleFormatTimestamp();
							// 测温仪 每个通道的信息
							for (int i = 0; i < num; i++) {
								DataAcquisitionVo d = new DataAcquisitionVo();
								d.setEquipmentId(equipmentId);
								d.setEquipmentName(equipmentName);
								d.setAddress(frameStru);
								d.setReceiveTime(receiveTime);
								d.setDutyPerson(e.getUserName());
								d.setTel(e.getTel());
								d.setNumOfCh(e.getNumOfCh());
								Float temp = Integer.valueOf(inputString.substring(i * 8, i * 8 + 4), 16) / 10F;
								Integer pd = Integer.valueOf(inputString.substring(i * 8 + 4, i * 8 + 6), 16);
								Integer uv = Integer.valueOf(inputString.substring(i * 8 + 6, i * 8 + 8), 16);
								d.setChannelNum(cnl.get(i));
								d.setOpticalFiberPosition(opticall.get(i));
								d.setTemperature(temp.toString());
								d.setPd(pd.toString());
								d.setUv(uv.toString());
								switch (temp.toString()) {
								case "3000.0":
									d.setState(2);
									d.setTemperature("3000");
									d.setMessage("光纤故障");
									break;
								case "6116.6":
									d.setState(1);
									d.setTemperature("-437");
									d.setMessage("传感器模块故障");
									break;
								case "2999.9":
									d.setState(3);
									d.setTemperature("2999");
									d.setMessage("系统调整中");
									break;
								default:
									d.setState(5);
									d.setMessage("正常");
									break;
								}
								if (temp != 3000.0F && temp != -6116.6F && temp != 2999.9F && temp != 6548.3F
										&& (temp < Float.valueOf(minl.get(i)) || temp > Float.valueOf(maxl.get(i)))) {
									d.setState(9);
								}
								// 往SQL server中插入数据，为了找问题方便看
								dam.insertSQL(d);
								// 往Hbase中插入数据
								das.insertHbase(d);
								realTimeDateList.add(d);
							}
							// 往前台推送
							simpMessage.convertAndSend("/topic/serialport", realTimeDateList);
							log.info("realTimeDateList的size----" + realTimeDateList.size());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 添加监听器
	 * 
	 * @param port
	 *            串口对象
	 * @param listener
	 *            串口存在有效数据监听
	 */
	public static void addListener(SerialPort serialPort, DataAvailableListener listener) {
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
	public static class SerialPortListener implements SerialPortEventListener {

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
