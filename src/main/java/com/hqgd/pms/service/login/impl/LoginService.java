package com.hqgd.pms.service.login.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.login.ILoginService;
import com.hqgd.pms.service.user.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginService implements ILoginService {

	@Autowired
	IUserService userService;

	@Override
	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("username").trim();
		String password = request.getParameter("password").trim();
		boolean isRemember = Boolean.valueOf(request.getParameter("isRemember"));
		log.info("登录开始，参数为userName=" + userName + "password=" + password + "isRemember=" + isRemember);
		User userFind = userService.selectByUserName(userName);
		Map<String, Object> map = authUser(userFind,
				new User(null, userName, null, password, null, null, null, null, null, null));
		Boolean authUser = (Boolean) map.get("success");
		if (authUser) {
			log.info("用户名密码正确");
			// 记住密码
			if (isRemember) {
				Cookie cookie1 = new Cookie("username", userName);
				cookie1.setPath("/");
				cookie1.setMaxAge(60 * 60 * 24 * 30);
				response.addCookie(cookie1);
				Cookie cookie2 = new Cookie("password", password);
				cookie2.setPath("/");
				cookie2.setMaxAge(60 * 60 * 24 * 30);
				response.addCookie(cookie2);
				Cookie cookie3 = new Cookie("password", password);
				cookie3.setPath("/");
				cookie3.setMaxAge(60 * 60 * 24 * 30);
				response.addCookie(cookie3);
			}
			request.getSession().setAttribute("user", userFind);

			Integer id = userFind.getId();
			Boolean b = userService.isSystemUser(id);
			// 密码校验通过之后，判断登陆的用户的权限性质，如果是管理员，跳转至管理页面，如果是普通用户跳转至用户首页
			// 1表示管理员，0表示普通用户
			if (b) {
				map.put("roleId", "1");
			} else {
				map.put("roleId", "0");
			}
			map.put("userId", id);
			map.put("userName", userName);
		}
		return map;
	}

	private Map<String, Object> authUser(User userFind, User userNew) {
		String message = null;
		boolean success = true;
		if (StringUtils.isEmpty(userNew.getUserName()) || StringUtils.isEmpty(userNew.getPassword())) {
			message = "用户名密码不能为空!";
			success = false;
		} else {
			if (userFind == null || userFind.getIsdel().equals("Y")) { // 系统中没查到用户输入的用户名的相关信息
				message = "用户名不存在或已被禁用！";
				success = false;
			} else if (!userFind.getPassword().equals(userNew.getPassword())) { // 用户名或密码输入不正确
				message = "用户名或密码不正确！";
				success = false;
			} else {
				success = true;
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("success", success);
		return map;
	}

}
