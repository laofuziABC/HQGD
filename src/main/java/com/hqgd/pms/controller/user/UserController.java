package com.hqgd.pms.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.user.IUserService;

@Controller
@Scope("request")
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
	public void update(Model model, User user, String add, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		User userLog = (User) request.getSession(true).getAttribute("user");
		Map<String, Object> result = new HashMap<String, Object>();
		if (Boolean.valueOf(add)) {
			result = userService.add(user, userLog);
		} else {
			result = userService.update(user, userLog);
		}
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(result));

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
	public String selectByUserName(Model model, String userName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (userName == null) {
			return null;
		} else {
			List<User> userList = new ArrayList<User>();
			User user = userService.selectByUserName(userName);
			userList.add(user);
			String json = new Gson().toJson(userList).toString();
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
	public String updatePassword(@Param("id") String id, @Param("oldpassword") String oldpassword,
			@Param("newPassword") String newPassword) {

		Map<String, Object> result = new HashMap<>();
		result = userService.updatePassword(id, oldpassword, newPassword);
		String json = new Gson().toJson(result).toString();
		return json;
	}

	@RequestMapping(value = "resetPassword")
	public void resetPassword(Model model, HttpServletRequest request, HttpServletResponse response, String userName,
			String phone, String password, String newPassword) throws IOException {
		String message = userService.resetPassword(userName, phone, password, newPassword);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(message));
	}
	
	/**
	 * 根据当前用户，通过用户角色获取用户名称或者用户集
	 * 用户角色为管理员，返回其下用户集合
	 * 用户角色为普通用户，返回当前用户
	 */
	@RequestMapping("/session")
	public void getUsersFromSession(Model model, HttpServletRequest request, 
			HttpServletResponse response) throws ExecutionException, InterruptedException, IOException{
		
		List<User> userList = new ArrayList<User>();
		//获取当前Session，根据用户角色返回结果
		HttpSession session = request.getSession();
		if(session != null) {
			User user = (User) session.getAttribute("user");
			if(userService.isSystemUser(user.getId())) {
				userList=userService.selectAll();
			}else{
				userList.add(user);
			}
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write(new Gson().toJson(userList));
		}
	}
	
}