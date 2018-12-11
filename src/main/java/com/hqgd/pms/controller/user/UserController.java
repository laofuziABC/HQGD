package com.hqgd.pms.controller.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@RequestMapping("delete")
	@ResponseBody
	public String delete(Model model, String userId, HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> result = userService.delete(userId);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping(value = "update") // 新增和编辑用户信息都是此接口
	@ResponseBody
	public String update(Model model, User user, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User userLog = (User) request.getSession(true).getAttribute("user");
		// User userLog = user;
		// Map<String, Object> result = userService.update(user, userLog);
		Map<String, Object> result = new HashMap<String, Object>();
		if (user.getId() == null) {
			result = userService.add(user, userLog);
		} else {
			result = userService.update(user, userLog);
		}
		String json = new Gson().toJson(result).toString();
		return json;

	}

	@RequestMapping(value = "select")
	@ResponseBody
	public String select(Model model, String userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (userId == null) {
			return null;
		} else {
			User user = userService.select(userId);
			String json = new Gson().toJson(user).toString();
			return json;
		}
	}

	@RequestMapping(value = "selectByUserName")
	@ResponseBody
	public String selectByUserName(Model model, String userName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		if (userName == null) {
			return null;
		} else {
			User user = userService.selectByUserName(userName);
			String json = new Gson().toJson(user).toString();
			return json;
		}
	}

	@RequestMapping(value = "selectAll")
	@ResponseBody
	public String selectAll(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		List<User> user = userService.selectAll();
		String json = new Gson().toJson(user).toString();
		return json;

	}

	@RequestMapping(value = "initUserPassword")
	@ResponseBody
	public String initUserPassword(Model model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userId = request.getParameter("userId");
		Map<String, Object> result = new HashMap<String, Object>();
		result = userService.initUserPassword(userId);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping(value = "updatePassword")
	@ResponseBody
	public String updatePassword(String id, String oldpassword, String newPassword) {

		Map<String, Object> result = new HashMap<>();
		result = userService.updatePassword(id, oldpassword, newPassword);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping("/recordExport")
	public void recordExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String path = request.getParameter("path");
		String classPath = userService.execRecordExport(path);
		try {
			try {
				path = new String(classPath.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			// response.setContentType("application/octet-stream;charset=ISO8859-1");
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
}