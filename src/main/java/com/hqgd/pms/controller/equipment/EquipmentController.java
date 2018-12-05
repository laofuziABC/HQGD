package com.hqgd.pms.controller.equipment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.EquipmentParam;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IEquipmentService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚绒 设备管理，增删改查
 */
@Controller
@Scope("request")
@Slf4j
@RequestMapping("equipment")
public class EquipmentController {
	@Autowired
	IEquipmentService equipmentService;

	@RequestMapping(value = "/add")
	public void add(Model model, User user,EquipmentInfo equipmentInfo, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("添加设备开始");
		User loginUser = user;
//		User loginUser = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> resultMap = equipmentService.add(equipmentInfo, loginUser);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("添加设备结束");
	}

	@RequestMapping(value = "/delete")
	public void delete(Model model, String equipmentId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("删除设备开始");
		Map<String, Object> resultMap = equipmentService.delete(equipmentId);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("删除设备结束");
	}

	@RequestMapping(value = "/update")
	public void update(Model model, EquipmentInfo equipmentInfo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("更新设备开始");
		User loginUser = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> resultMap = equipmentService.update(equipmentInfo, loginUser);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("更新设备结束");
	}

	@RequestMapping(value = "/select")
	public void select(Model model, String equipmentId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("查询设备开始");
		EquipmentInfo equipmentInfo = equipmentService.select(equipmentId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000004");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", equipmentInfo == null ? "null" : equipmentInfo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询设备结束");
	}

	@RequestMapping(value = "/selectAll")
	@ResponseBody
	public void selectAll(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("查询所有设备开始");
		User user = (User) request.getSession(true).getAttribute("user");
		String userId = user.getId().toString();
		// String userId = "1";
		List<EquipmentInfo> equipmentList = equipmentService.selectAll(userId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000005");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", equipmentList);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询所有设备结束");
	}

	@RequestMapping(value = "/equipmentParam")
	public void selectEquipmentParam(Model model, String equipmentId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("查询设备参数开始 ，equipmentId =  " + equipmentId);
		List<EquipmentParam> equipmentParam = equipmentService.selectEquipmentParam(equipmentId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000006");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", equipmentParam);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询设备参数结束 ");
	}

	@RequestMapping(value = "/setParam")
	public void setEquipmentParam(Model model, EquipmentParam equipmentParam, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("设置设备参数开始, request = " + request);
		User loginUser = (User) request.getSession(true).getAttribute("user");
		Boolean rusult = equipmentService.setEquipmentParam(equipmentParam,loginUser);
		if(rusult) {
			response.sendRedirect("index.html");
		}
//		response.setContentType("application/json; charset=UTF-8");
//		response.getWriter().write(new Gson().toJson(rusultMap));
		log.info("设置设备参数结束 ");
	}
	
	@RequestMapping(value = "/updateParam")
	public void updateParam(Model model, EquipmentParam equipmentParam, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("更新设备参数开始, request = " + request);
		User loginUser = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> rusultMap = equipmentService.updateParam(equipmentParam,loginUser);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(rusultMap));
		log.info("更新设备参数结束 ");
	}
}
