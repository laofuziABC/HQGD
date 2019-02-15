package com.hqgd.pms.tcp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
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
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	private static final long serialVersionUID = -6915004878346440376L;

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
		}, 0, 10000);

	}
	
	private void doExcute(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 拿到所有客户端信息
		List<Socket> socketlist = MultiThreadSocketServer.CLIENT_SOCKET_LIST;
		for (Socket socket : socketlist) {
			// 循环获取信息
			ClientSocketHandler.sendMessage(new BufferedOutputStream(socket.getOutputStream()), "Are you OK ?");
		}
		resp.getWriter().println("test OK");
	}

}
