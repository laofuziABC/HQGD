package com.hqgd.pms.controller.system;

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

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.domain.SysParam;
import com.hqgd.pms.service.system.ISystemService;
import com.hqgd.pms.service.user.IUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 姚绒 设备管理，增删改查
 */
@Controller
@Scope("request")
@Slf4j
@RequestMapping("system")
public class SystemController {
	@Autowired
	ISystemService systemService;
	@Autowired
	IUserService userService;

	@RequestMapping(value = "/setSysParam")
	public void setSysParam(Model model, String paramCode,String paramValue, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("设置参数开始");
		Map<String, Object> resultMap = systemService.setSysParam(paramCode,paramValue);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("设置参数结束");
	}
	@RequestMapping(value = "/selectSysParam")
	public void selectSysParam(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		log.info("查询参数开始");
		List<SysParam> sysParam = systemService.selectSysParam();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000005");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		resultMap.put("data", sysParam);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
		log.info("查询参数结束");
	}

}
