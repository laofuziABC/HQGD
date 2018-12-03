package com.hqgd.pms.controller.user;

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
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@RequestMapping("add")
	@ResponseBody
	public String add(Model model, User user, HttpServletRequest request, HttpServletResponse response) {
		User userLog = (User) request.getSession(true).getAttribute("user");
//		User userLog = user;
		Map<String, Object> result = userService.add(user, userLog);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping("delete")
	@ResponseBody
	public String delete(Model model, String userId, HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> result = userService.delete(userId);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping(value = "update")
	@ResponseBody
	public String update(Model model, User user, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User userLog = (User) request.getSession(true).getAttribute("user");
		// User userLog = user;
//		Map<String, Object> result = userService.update(user, userLog);
		Map<String, Object> result = new HashMap<String, Object>();
		if(user.getId() == null) {
			result = userService.add(user, userLog);
		}else {
			result = userService.update(user, userLog);
		}
		String json = new Gson().toJson(result).toString();
		return json;

	}

	@RequestMapping(value = "select")
	@ResponseBody
	public String select(Model model, String userId, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		if(userId == null) {
			return null;
		}else {
			User user = userService.select(userId);
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

}