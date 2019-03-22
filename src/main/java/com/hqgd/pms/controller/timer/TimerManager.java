package com.hqgd.pms.controller.timer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.system.SysParamMapper;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

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
	@Autowired
	private IDataAcquisitionService dataAcquisitionService;
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
	 * 服务器向客户端发送请求数据命令定时任务
	 */
	@Autowired
	private YouXinConfiguration youXinConfiguration;

	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	private ScheduledFuture<?> future1;

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

	@PostMapping("/failures/start")
	public void startFailuresTask1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("统计故障次数定时器开始");
		resp.setContentType("application/json; charset=UTF-8");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String startTime = format.format(c.getTime());// 前一天
		if (future1 == null) {
			future1 = threadPoolTaskScheduler.schedule(new StaticFailuresTask(startTime), new Trigger() {
				@Override
				public Date nextExecutionTime(TriggerContext triggerContext) {
					return new CronTrigger(youXinConfiguration.getCorn1()).nextExecutionTime(triggerContext);
				}
			});
		} else {
			log.info("定时器已经处于启动状态！");
			resp.getWriter().write(new Gson().toJson("定时器已经处于启动状态！"));
		}
	}

	@PostMapping("/failures/stop")
	public void stopCron1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (future1 != null) {
			future1.cancel(true);
		}
		future1 = null;
		log.info("统计故障次数定时器关闭！");
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("统计故障次数定时器关闭！"));
	}

	// @RequestMapping("/failures/start")
	// public void startFailuresTask(HttpServletRequest req, HttpServletResponse
	// resp) throws IOException {
	// log.info("统计故障次数定时器开始");
	// resp.setContentType("application/json; charset=UTF-8");
	// timer.purge();
	// SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	// Calendar c = Calendar.getInstance();
	// c.add(Calendar.DATE, -1);
	// String startTime = format.format(c.getTime()) + " 00:00:00";// 前一天
	// if (staticFailuresTask == null) {
	// staticFailuresTask = new StaticFailuresTask(startTime);
	// Timer timer = new Timer();
	// timer.schedule(staticFailuresTask, 0, 60 * 60 * 24 * 1000);
	// resp.getWriter().write(new Gson().toJson("操作成功！"));
	// } else {
	// resp.getWriter().write(new Gson().toJson("定时器已经处于启动状态！"));
	// }
	// log.info("统计故障次数定时器结束");
	//
	// }

	@RequestMapping("/failures/run")
	public void runFailuresTask(HttpServletRequest req, String startTime, String endTime, HttpServletResponse resp)
			throws IOException, ParseException {
		log.info("统计故障次数开始");
		timer.purge();
		// 结束时间和开始时间进行比较，大于等于就继续执行
		while (endTime.compareTo(startTime) >= 0) {
			String afterDay = CommonUtil.getAfterDay(startTime);
			dataAcquisitionService.run(startTime);
			// 每执行一次，开始时间都要
			startTime = afterDay;
		}
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("操作成功！"));
		log.info("统计故障次数结束");

	}

	// @RequestMapping("/failures/stop")
	// public void stopFailuresTask(HttpServletRequest req, HttpServletResponse
	// resp) throws IOException {
	// staticFailuresTask.cancel();
	// staticFailuresTask = null;// 如果不重新new，会报异常
	// log.info("统计故障次数定时器关闭！");
	// resp.setContentType("application/json; charset=UTF-8");
	// resp.getWriter().write(new Gson().toJson("统计故障次数定时器关闭！"));
	// }

}