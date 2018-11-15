package com.hqgd.pms.controller.equipment;

import java.io.IOException;
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
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.equipment.IEquipmentService;

/**
 * @author 姚绒 设备管理，增删改查
 */
@Controller
@Scope("request")
@RequestMapping("equipment")
public class EquipmentController {
	@Autowired
	IEquipmentService equipmentService;

	@RequestMapping(value = "/add")
	public String add(Model model, EquipmentInfo equipmentInfo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Map<String, Object> result = equipmentService.add(equipmentInfo);
		String json = new Gson().toJson(result).toString();
		return json;

	}

	@RequestMapping(value = "/delete")
	public String delete(Model model, String equipmentId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Map<String, Object> result = equipmentService.delete(equipmentId);
		String json = new Gson().toJson(result).toString();
		return json;

	}

	@RequestMapping(value = "/update")
	public String update(Model model, EquipmentInfo equipmentInfo, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Map<String, Object> result = equipmentService.update(equipmentInfo);
		String json = new Gson().toJson(result).toString();
		return json;

	}

	@RequestMapping(value = "/select")
	public String select(Model model, String equipmentId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		EquipmentInfo equipmentInfo = equipmentService.select(equipmentId);
		String json = new Gson().toJson(equipmentInfo).toString();
		return json;

	}

	@RequestMapping(value = "/selectAll")
	@ResponseBody
	public Map<String, Object> selectAll(Model model, String param, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

//		List<EquipmentInfo> equipmentList = equipmentService.selectAll(param);
//		String json = new Gson().toJson(equipmentList).toString();
//		return json;
		return null;

	}
}
