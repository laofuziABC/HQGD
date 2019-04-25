package com.hqgd.pms.controller.dataAcquisition;

import java.io.IOException;
import java.text.ParseException;
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
		Map<String, Object> resultMap = dataAcquisitionService.getHistoricalData(queryVo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		long outTime = System.currentTimeMillis();
		log.info("查询历史数据结束：" + outTime);
		long midTime = outTime - inTime;
		log.info("接口访问时长为：" + midTime);
	}

	@RequestMapping("/realTimeCurve")
	public void realTimeCurve(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException, ParseException {
		long inTime = System.currentTimeMillis();
		log.info("查询实时曲线开始 " + inTime);
		Map<String, Object> resultList = dataAcquisitionService.realTimeCurve(queryVo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultList));
		long outTime = System.currentTimeMillis();
		log.info("实时曲线接口访问时长为：" + (outTime - inTime));
	}

	@RequestMapping("/historicalCurve")
	public void historicalCurve(Model model, QueryParametersVo queryVo, HttpServletRequest request,
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException, ParseException {
		long inTime = System.currentTimeMillis();
		log.info("查询历史曲线开始 " + inTime);
		Map<String, Object> resultList = dataAcquisitionService.historicalCurve(queryVo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultList));
		long outTime = System.currentTimeMillis();
		log.info("历史曲线接口访问时长为：" + (outTime - inTime));
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
	 * 通过设备主键，获取指定时间段内的通道温度最值 结果用于设备最值统计表 03.26
	 * 
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
