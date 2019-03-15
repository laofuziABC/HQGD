package com.hqgd.pms.controller.timer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.TimerTask;

import com.hqgd.pms.tcp.MultiThreadSocketServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketTimer extends TimerTask {

	@Override
	public void run() {
		try {
			doExcute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doExcute() throws IOException {
		// 拿到所有客户端信息
		List<Socket> socketlist = MultiThreadSocketServer.CLIENT_SOCKET_LIST;
		log.info("socketlist的size=" + socketlist.size());
		// List<EquipmentInfo> equipmentList = equipmentInfoMapper.selectAll();
		// log.info("测温仪的size=" + socketlist.size());
		// List<String> frameList = new ArrayList<String>();
		// for (EquipmentInfo e : equipmentList) {
		// int f = Integer.valueOf(e.getFrameStru());
		// int n = e.getNumOfCh();
		// String fHex = Integer.toHexString(f);
		// String nHex = Integer.toHexString(n * 2);
		// if (fHex.length() < 2) {
		// fHex = "0" + fHex;
		// }
		// if (nHex.length() < 2) {
		// nHex = "0" + nHex;
		// }
		// String frame = fHex + " 03 01 48 00 " + nHex;
		// String rameStru = getCRC(frame);
		//
		// frameList.add(frame + " " + rameStru);
		// }
		// for (String s : frameList) {
		for (Socket socket : socketlist) {
			// 循环获取信息
			byte[] bytes = hexStringToByteArray("01030148000CC425");
			OutputStream os = socket.getOutputStream();
			os.write(bytes);
			// new PrintStream(socket.getOutputStream()).println(s);
		}
		// }
	}

	public static String getCRC(String data) {
		data = data.replace(" ", "");
		int len = data.length();
		if (!(len % 2 == 0)) {
			return "0000";
		}
		int num = len / 2;
		byte[] para = new byte[num];
		for (int i = 0; i < num; i++) {
			int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
			para[i] = (byte) value;
		}
		return getCRC(para);
	}

	/**
	 * 计算CRC16校验码
	 *
	 * @param bytes
	 *            字节数组
	 * @return {@link String} 校验码
	 * @since 1.0
	 */
	public static String getCRC(byte[] bytes) {
		// CRC寄存器全为1
		int CRC = 0x0000ffff;
		// 多项式校验值
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		// 结果转换为16进制
		String result = Integer.toHexString(CRC).toUpperCase();
		if (result.length() != 4) {
			StringBuffer sb = new StringBuffer("0000");
			result = sb.replace(4 - result.length(), 4, result).toString();
		}
		//
		// return result.substring(2, 4) + " " + result.substring(0, 2);
		// 交换高低位，低位在前高位在后
		return result.substring(2, 4) + " " + result.substring(0, 2);
	}

	public static byte[] hexStringToByteArray(String hexString) {
		hexString = hexString.replaceAll(" ", "");
		int len = hexString.length();
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
			bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return bytes;
	}

}