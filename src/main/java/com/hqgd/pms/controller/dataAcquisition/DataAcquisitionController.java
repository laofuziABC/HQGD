package com.hqgd.pms.controller.dataAcquisition;

import java.io.IOException;
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
import com.hqgd.pms.common.PayCommon;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
@RequestMapping("/dataAcquisition")
public class DataAcquisitionController {

	@Autowired
	@Qualifier("dataAcquisitionService")
	private IDataAcquisitionService dataAcquisitionService;

	@RequestMapping(value = "/realtime")
	public void getRealTimeMonitoringData(Model model, String equipmentId, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
		log.info("查询实时数据开始,equipmentId=" + equipmentId);
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionService.execGetRealTimeData(equipmentId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", PayCommon.getSimpleFormatTimestamp());
		resultMap.put("message", "查询实时数据成功");
		resultMap.put("data", realTimeDateList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询实时数据结束，list = " + resultMap);
	}

	@RequestMapping(value = "/historical")
	public void getHistoricalData(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
		log.info("查询历史数据开始,queryVo=" + queryVo);
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionService.getHistoricalData(queryVo);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", PayCommon.getSimpleFormatTimestamp());
		resultMap.put("message", "查询历史数据成功");
		resultMap.put("data", historicalDataList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询历史数据结束 ");
	}
}
