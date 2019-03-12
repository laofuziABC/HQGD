package com.hqgd.pms.controller.timer;

import java.io.IOException;
import java.util.Timer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.dao.system.SysParamMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务管理器
 * 
 * @author yr
 *
 */
@Slf4j
@Controller
@RequestMapping("/timer")
public class TimerManager {
	@Resource
	private SysParamMapper sysParamMapper;
	/**
	 * 单例模式
	 */
	private static TimerManager timerManager = null;

	private TimerManager() {
	}

	public static TimerManager getInstance() {
		if (timerManager == null) {
			timerManager = new TimerManager();
		}
		return timerManager;
	}

	/**
	 * 定时器
	 */
	private Timer timer = new Timer();

	/**
	 * 更新临时表的定时任务
	 */
	private UpdateTableTimer tableTask = null;

	/**
	 * 服务器向客户端发送请求数据命令定时任务
	 */
	private SocketTimer socketTask = null;

	/**
	 * 启动定时任务
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/table/start")
	public void startTimerTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("创建临时表定时器开始");
		resp.setContentType("application/json; charset=UTF-8");
		timer.purge();
		if (tableTask == null) {
			tableTask = new UpdateTableTimer();

			// 从系统参数获取定时器的执行间隔
			int timeInter = Integer.valueOf(sysParamMapper.selectByPrimaryKey("TIME_INTERVAL").getParamValue());
			Timer timer = new Timer();
			timer.schedule(tableTask, 0, timeInter * 1000);
			resp.getWriter().write(new Gson().toJson("操作成功！"));
		} else {
			resp.getWriter().write(new Gson().toJson("定时器已经处于启动状态！"));
		}
		log.info("创建临时表定时器结束");

	}

	/**
	 * 定时任务取消
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/table/stop")
	public void stopTimerTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		tableTask.cancel();
		tableTask = null;// 如果不重新new，会报异常
		log.info("定时器关闭！");
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("定时器关闭！"));
	}

	/**
	 * 启动定时任务
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/socket/start")
	public void startSocketTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("服务器向客户端发送请求数据命令定时器开始");
		resp.setContentType("application/json; charset=UTF-8");
		timer.purge();
		if (socketTask == null) {
			socketTask = new SocketTimer();

			// 从系统参数获取定时器的执行间隔
			int timeInter = Integer.valueOf(sysParamMapper.selectByPrimaryKey("TIME_INTERVAL").getParamValue());
			Timer timer = new Timer();
			timer.schedule(socketTask, 0, timeInter * 1000);
			resp.getWriter().write(new Gson().toJson("操作成功！"));
		} else {
			resp.getWriter().write(new Gson().toJson("定时器已经处于启动状态！"));
		}
		log.info("服务器向客户端发送请求数据命令定时器结束");

	}

	/**
	 * 定时任务取消
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/socket/stop")
	public void stopSocketTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		socketTask.cancel();
		socketTask = null;// 如果不重新new，会报异常
		log.info("服务器向客户端发送请求数据命令定时器关闭！");
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("服务器向客户端发送请求数据命令定时器关闭！"));
	}

}