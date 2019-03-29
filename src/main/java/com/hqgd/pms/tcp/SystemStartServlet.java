package com.hqgd.pms.tcp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <pre>
 *  描述：启动类，保证tomcat启动后初始化开始
 * @date: 2019年2月1日 上午10:43:36
 * @author: yaorong 
 * @ClassName: SystemStartServlet 
 * </pre>
 */
@Controller
@RequestMapping("tcp")
public class SystemStartServlet extends HttpServlet {

	private static final long serialVersionUID = 5037729655672304588L;

	@RequestMapping(value = "/startServer")
	public void init() throws ServletException {
		System.out.println("SystemStartServlet init Start...");
		// 启动MultiThreadSocketServer服务监听类
		new Thread(new MultiThreadSocketServer()).start();
	}

}
