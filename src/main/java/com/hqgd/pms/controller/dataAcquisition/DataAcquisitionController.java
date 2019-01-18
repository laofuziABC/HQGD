package com.hqgd.pms.controller.dataAcquisition;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

	@RequestMapping("/allEquipRealtime")
	public void allEquipRealtime(Model model, String userName,String roleId, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
		long inTime = System.currentTimeMillis();
		log.info("查询所有设备实时数据开始 " + inTime);
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionService.allEquipRealtime(userName,roleId);
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
	 * 获取指定时间段（自开机到当前时间）内的数据点的集合
	 * 用于初始化当前监控图表
	 * 2018.12.25
	 */
	@RequestMapping("/periodDate")
	public void getPeriodDate(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long inTime = System.currentTimeMillis();
		log.info("periodDate接口开始：" + inTime);
		Map<String, Object> resultList = dataAcquisitionService.getPeriodDataByQuery(queryVo);
		long outTime = System.currentTimeMillis();
		log.info("periodDate接口结束：" + outTime);
		log.info("接口访问时长：" + (outTime-inTime));
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultList));
	}

	/**
	 * 描述： 作者：姚绒 日期：2018年11月20日 下午1:53:51 @param data @return @throws Exception
	 * Model @throws
	 */
	@RequestMapping("/recordExport")
	public void recordExport(QueryParametersVo data ,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = request.getParameter("path");
		String classPath = dataAcquisitionService.execRecordExport(data, path);
		try {
			try {
				path = new String(classPath.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/vnd.ms-excel;charset=gb2312"); 
//			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + path);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("success", Boolean.TRUE.toString());
			resultMap.put("resultCode", "00000000");
			resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
			resultMap.put("message", "查询历史数据成功");
			resultMap.put("data", path);
			response.getWriter().write(new Gson().toJson(resultMap));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@RequestMapping("/record")
	public void record(QueryParametersVo data ,HttpServletRequest request,HttpServletResponse response) throws Exception {
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
