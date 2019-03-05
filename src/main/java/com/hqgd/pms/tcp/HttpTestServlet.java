package com.hqgd.pms.tcp;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;

/**
 * <pre>
 *  描述：辅助测试类，收集温度
 * &#64;date: 2019年2月1日 下午5:26:15
 * &#64;author: yaorong 
 * &#64;ClassName: HttpTestServlet
 * </pre>
 */
@Controller
@RequestMapping("socket_server")
public class HttpTestServlet extends HttpServlet {
	@Resource
	private  DataAcquisitionVoMapper dataAcquisitionVoMapper;
	private static final long serialVersionUID = -6915004878346440376L;
	@Resource
	private  EquipmentInfoMapper equipmentInfoMapper;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doExcute(req, resp);
	}

	@RequestMapping(value = "/test")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					doExcute(req, resp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 60000);

	}

	private void doExcute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 拿到所有客户端信息
		List<Socket> socketlist = MultiThreadSocketServer.CLIENT_SOCKET_LIST;
		List<EquipmentInfo> equipmentList = equipmentInfoMapper.selectAll();
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
			String rameStru = getCRC(frame);
			frameList.add(frame + " " + rameStru);
		}
		for (String s : frameList) {
			for (Socket socket : socketlist) {
				// 循环获取信息
				new PrintStream(socket.getOutputStream()).println(s);
			}
		}
		resp.getWriter().println("test OK");
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

}
