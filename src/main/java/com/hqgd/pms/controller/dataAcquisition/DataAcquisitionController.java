package com.hqgd.pms.controller.dataAcquisition;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：数据采集 作者：姚绒 日期：2018年11月20日 下午1:49:37
 *
 */

@Slf4j
@Controller
@Scope("request")
@RequestMapping("/dataAcquisition")
public class DataAcquisitionController {

	@Autowired
	@Qualifier("dataAcquisitionService")
	private IDataAcquisitionService dataAcquisitionService;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

	@RequestMapping("/realtime")
	public void getRealTimeMonitoringData(Model model, String equipmentId, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
		long inTime = System.currentTimeMillis();
		log.info("查询实时数据开始 " + inTime);
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionService.execGetRealTimeData(equipmentId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "查询实时数据成功");
		resultMap.put("data", realTimeDateList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		long outTime = System.currentTimeMillis();
		log.info("查询实时数据结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("接口访问时长为：" + midTime);
	}

	// 飘窗报警需要知道所有设备的实时数据
	@RequestMapping("/allEquipRealtime")
	public void allEquipRealtime(Model model, String userName, String roleId, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
		long inTime = System.currentTimeMillis();
		log.info("查询所有设备实时数据开始 " + inTime);
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionService.allEquipRealtime(userName, roleId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "查询实时数据成功");
		resultMap.put("data", realTimeDateList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		long outTime = System.currentTimeMillis();
		log.info("查询所有设备实时数据结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("接口访问时长为：" + midTime);
	}

	@RequestMapping("/historical")
	public void getHistoricalData(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
		long inTime = System.currentTimeMillis();
		log.info("查询历史数据开始 " + inTime);
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionService.getHistoricalData(queryVo);
		Integer total = dataAcquisitionService.selectTotal(queryVo);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "查询历史数据成功");
		resultMap.put("data", historicalDataList);
		resultMap.put("total", total);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		long outTime = System.currentTimeMillis();
		log.info("查询历史数据结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("接口访问时长为：" + midTime);
	}

	@RequestMapping("/historicalCurve")
	public void historicalCurve(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long inTime = System.currentTimeMillis();
		log.info("查询历史数据曲线开始 " + inTime);
		Map<String, Object> historicalDataList = dataAcquisitionService.historicalCurve(queryVo);
		long outTime = System.currentTimeMillis();
		log.info("查询历史数据曲线结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("接口访问时长为：" + midTime);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "查询历史数据成功");
		resultMap.put("data", historicalDataList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));

	}

	/**
	 * 获取指定时间段（自开机到当前时间）内的数据点的集合 用于初始化当前监控图表 2018.12.25
	 */
	@RequestMapping("/periodDate")
	public void getPeriodDate(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long inTime = System.currentTimeMillis();
		log.info("periodDate接口开始：" + inTime);
		Map<String, Object> resultList = dataAcquisitionService.getPeriodDataByQuery(queryVo);
		long outTime = System.currentTimeMillis();
		log.info("periodDate接口结束：" + outTime);
		log.info("接口访问时长：" + (outTime - inTime));
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultList));
	}

	// 创建临时表
	@RequestMapping(value = "/temporaryTable")
	public void temporaryTable() {
		// 获取服务启动时间
		long startTime = System.currentTimeMillis();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				// 获取当前时间
				long currentTime = System.currentTimeMillis();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MINUTE, -1460);
				String lastTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
				Map<String, Object> param = new HashMap<>();
				// 判断当前时间和登录时间的差额,如果小于一天就把临时表1里的数据放进临时表2,如果大于一天就把临时表2里的最旧一条删除，再插入最新的一条
				if ((currentTime - startTime) > (24 * 3600000 + 1000 * 60 * 20)) {// 保留十分钟的冗余度，避免系统时间和数据库时间有误差
					for (int i = 1; i <= 4; i++) {
						param.put("lastTime", lastTime);
						param.put("table", "hq_equipment_monitor_data_r" + i);
						dataAcquisitionVoMapper.deleter(param);
					}
				}
				for (int i = 1; i <= 4; i++) {
					param.put("tabler", "hq_equipment_monitor_data_r" + i);
					param.put("tablef", "hq_equipment_monitor_data_f" + i);
					dataAcquisitionVoMapper.insertt(param);
				}
			}
		}, 0, 60000);
	}

	@RequestMapping("/record")
	public void record(QueryParametersVo data, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionService.record(data);
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", Boolean.TRUE.toString());
			resultMap.put("resultCode", "00000000");
			resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
			resultMap.put("message", "");
			resultMap.put("data", historicalDataList);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(new Gson().toJson(resultMap));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
