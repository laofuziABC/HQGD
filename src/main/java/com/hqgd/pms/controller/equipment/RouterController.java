package com.hqgd.pms.controller.equipment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hqgd.pms.domain.RouterInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IRouterService;

@Controller
@RequestMapping("/router")
public class RouterController {

	@Autowired
	private IRouterService routerService;

	@RequestMapping("delete")
	@ResponseBody
	public String delete(Model model, String routerId, HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> result = routerService.delete(routerId);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping(value = "update") // 新增和编辑用户信息都是此接口
	@ResponseBody
	public String update(Model model, RouterInfo routerInfo, boolean add, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		User userLog = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> result = new HashMap<String, Object>();
		if (add) {
			result = routerService.add(routerInfo, userLog);
		} else {
			result = routerService.update(routerInfo, userLog);
		}
		String json = new Gson().toJson(result).toString();
		return json;

	}

	@RequestMapping(value = "selectByRouterName")
	@ResponseBody
	public void selectByUserName(Model model, String routerName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		List<RouterInfo> router = routerService.selectByRouterName(routerName);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(router));
	}

	@RequestMapping(value = "selectAll")
	@ResponseBody
	public void selectAll(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<RouterInfo> router = routerService.selectAll();
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(router));

	}

	@RequestMapping(value = "selectConEqui")
	@ResponseBody
	public void selectConEqui(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<Map<String, String>> router = routerService.selectConEqui();
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(router));

	}

}