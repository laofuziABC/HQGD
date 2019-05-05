package com.hqgd.pms.controller.timer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.dao.system.SysParamMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：定时任务管理器 作者：姚绒 日期：2019年3月13日 下午3:04:00
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
	 * 串口通信发送请求数据命令定时任务
	 */
	private SerialPortTimer serialPortTimer = null;

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		return new ThreadPoolTaskScheduler();
	}

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
			Map<String, Object> param = new HashMap<>();
			param.put("paramCode", "UPDATE_TABLE_TIMER");
			param.put("paramValue", "1");
			int u = sysParamMapper.setSysParam(param);
			if (u > 0) {
				// 从系统参数获取定时器的执行间隔
				int timeInter = Integer.valueOf(sysParamMapper.selectByPrimaryKey("TIME_INTERVAL").getParamValue());
				Timer timer = new Timer();
				timer.schedule(tableTask, 0, timeInter * 1000);
				resp.getWriter().write(new Gson().toJson("操作成功！"));
			}
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
		Map<String, Object> param = new HashMap<>();
		param.put("paramCode", "UPDATE_TABLE_TIMER");
		param.put("paramValue", "0");
		sysParamMapper.setSysParam(param);
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
			Map<String, Object> param = new HashMap<>();
			param.put("paramCode", "TCP_CONNECT");
			param.put("paramValue", "1");
			sysParamMapper.setSysParam(param);
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
	 * 启动定时任务
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/serial/start")
	public void startSerialTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("串口发送请求数据命令定时器开始");
		resp.setContentType("application/json; charset=UTF-8");
		timer.purge();
		if (serialPortTimer == null) {
			serialPortTimer = new SerialPortTimer();
			Map<String, Object> param = new HashMap<>();
			param.put("paramCode", "TCP_CONNECT");
			param.put("paramValue", "1");
			sysParamMapper.setSysParam(param);
			// 从系统参数获取定时器的执行间隔
			int timeInter = Integer.valueOf(sysParamMapper.selectByPrimaryKey("TIME_INTERVAL").getParamValue());
			Timer timer = new Timer();
			timer.schedule(serialPortTimer, 0, timeInter * 1000);
			resp.getWriter().write(new Gson().toJson("操作成功！"));
		} else {
			resp.getWriter().write(new Gson().toJson("定时器已经处于启动状态！"));
		}
		log.info("串口发送请求数据命令定时器结束");

	}

	@RequestMapping("/serial/stop")
	public void stopSerialTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (serialPortTimer != null) {
			serialPortTimer.cancel();
			serialPortTimer = null;// 如果不重新new，会报异常
			Map<String, Object> param = new HashMap<>();
			param.put("paramCode", "TCP_CONNECT");
			param.put("paramValue", "0");
			sysParamMapper.setSysParam(param);
			log.info("串口发送请求数据命令定时器关闭！");
		}
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("串口发送请求数据命令定时器关闭！"));
	}

	/**
	 * 定时任务取消
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/socket/stop")
	public void stopSocketTask(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (socketTask != null) {
			socketTask.cancel();
			socketTask = null;// 如果不重新new，会报异常
			Map<String, Object> param = new HashMap<>();
			param.put("paramCode", "TCP_CONNECT");
			param.put("paramValue", "0");
			sysParamMapper.setSysParam(param);
			log.info("服务器向客户端发送请求数据命令定时器关闭！");
		}
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("服务器向客户端发送请求数据命令定时器关闭！"));
	}

}