package com.hqgd.pms.controller.dataAcquisition;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.hqgd.pms.dao.system.SysParamMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.domain.StaticFailures;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;
import com.hqgd.pms.service.equipment.IEquipmentService;

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
	@Resource
	private SysParamMapper sysParamMapper;
	@Autowired
	IEquipmentService equipmentService;

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
		Map<String, Object> historicalDataList = dataAcquisitionService.historicalCurve(queryVo);
		long outTime = System.currentTimeMillis();
		long midTime = outTime - inTime;
		log.info("历史数据曲线接口访问时长为：" + midTime);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(historicalDataList));
	}

	/**
	 * 获取指定时间段（自开机到当前时间）内的数据点的集合 用于初始化当前监控图表 2018.12.25
	 */
	@RequestMapping("/periodDate")
	public void getPeriodDate(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> resultList = dataAcquisitionService.getPeriodDataByQuery(queryVo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultList));
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

	@RequestMapping("/errorStateStatic")
	public void errorStateStatic(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.info("故障次数统计开始 ");
		List<StaticFailures> errorStateStatic = dataAcquisitionService.errorStateStatic(queryVo);
		EquipmentInfo equipmentInfo = equipmentService.select(queryVo.getEquipmentId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "故障次数统计成功");
		resultMap.put("data", errorStateStatic);
		resultMap.put("equipment", equipmentInfo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("故障次数统计开始 ");
	}
	
	/**
	 * 通过设备主键，获取指定时间段内的通道温度最值
	 * 结果用于设备最值统计表 03.26
	 * @param model
	 * @param queryVo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/historicalExtremum")
	public void extremun(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> resultMap = dataAcquisitionService.getHistoryExtremums(queryVo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
	}
}
