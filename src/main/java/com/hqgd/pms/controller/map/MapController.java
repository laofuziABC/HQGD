package com.hqgd.pms.controller.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.map.IMapService;

@Controller
@Scope("request")

@RequestMapping("/map")
public class MapController {
	@Autowired
	IMapService mapService;

	@ResponseBody
	@RequestMapping("/getPositions")
	public void getJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<EquipmentInfo> list = mapService.getLonLats(request);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", Boolean.TRUE.toString());
		resultMap.put("resultCode", "00000000");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "查询设备经纬度");
		resultMap.put("data", list);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));

	}

	public static void main(String[] args) {
		List<List<String>> ll = new ArrayList<List<String>>();
		List<String> l = new ArrayList<>();
		l.add("111");
		l.add("aaa");
		List<String> l1 = new ArrayList<>();
		l1.add("222");
		l1.add("bbb");
		ll.add(l);
		ll.add(l1);
		System.out.println(ll);
	}
}