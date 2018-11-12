package com.hqgd.pms.controller.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("logout")
public class LogOutController {

	/**
	 * 描述:退出登录 
	 * @return 
	 * 
	 */
	@RequestMapping("logout")
	public Map<String, Object> logOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("message", "ok");
		return result;
	}
}
