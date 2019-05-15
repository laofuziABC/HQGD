package com.hqgd.pms.testlaofuzi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
@RequestMapping("/monitors")
public class MonitorController {
	@Autowired
	private MonitorService dataService;

	/**
	 * 根据查询条件查询设备信息和设备最新温度监测信息
	 * @param provCode、cityCode、townCode 省市区地区编号
	 * @throws Exception 
	 */
	@RequestMapping("/equipDataInfos")
	public void getEquipmentInfos(int provCode, int cityCode, int townCode, Model model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		log.info("获取设备信息");
		//设置编码格式、组装地区编号属性
		request.setCharacterEncoding("UTF-8");
		int adcode = (provCode>cityCode)?provCode/10000:cityCode/100;
		adcode=(adcode>townCode)?adcode:townCode;
		request.setAttribute("adcode", adcode);
		List<Map<String,Object>> infoList = dataService.getInfosList(request);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(infoList));
	}
}
