package com.hqgd.pms.controller.timer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
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
@Component
@Order(value = 1)
@RequestMapping("/timer")
public class TimerManager implements ApplicationRunner {
	@Resource
	private SysParamMapper sysParamMapper;
	@Autowired
	private IDataAcquisitionService dataAcquisitionService;
	/**
	 * 单例模式
	 */
	private static TimerManager timerManager = null;

	public TimerManager() {
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
	public void startTimerTask() throws IOException {
		log.info("创建临时表定时器开始");
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
			}
		} else {
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

	@PostMapping("/failures/start")
	public void startFailuresTask1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		log.info("统计故障次数定时器开始");
		resp.setContentType("application/json; charset=UTF-8");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String startTime = format.format(c.getTime());// 前一天
		if (future1 == null) {
			Map<String, Object> param = new HashMap<>();
			param.put("paramCode", "STATIC_FAIL");
			param.put("paramValue", "1");
			sysParamMapper.setSysParam(param);
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
		Map<String, Object> param = new HashMap<>();
		param.put("paramCode", "STATIC_FAIL");
		param.put("paramValue", "0");
		sysParamMapper.setSysParam(param);
		resp.setContentType("application/json; charset=UTF-8");
		resp.getWriter().write(new Gson().toJson("统计故障次数定时器关闭！"));
	}

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

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("创建临时表定时器开始");
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
			}
		} else {
		}
		log.info("创建临时表定时器结束");

	}

}