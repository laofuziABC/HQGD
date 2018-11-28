package com.hqgd.pms.controller.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqgd.pms.service.login.ILoginService;
import com.hqgd.pms.service.user.IUserService;

/**
 * @author 姚绒
 *
 */
@Controller
public class LoginController {

	@Autowired
	IUserService userService;
	@Autowired
	ILoginService loginService;

	/**
	 * 描述：打开用户登陆界面 
	 * 作者：姚绒 日期：2018年11月5日 上午10:21:12 @param username @param password 
	 * @param response @param request @return String @throws
	 * @throws IOException 
	 */
	@RequestMapping("/hqgd")
	public String login(Model model, HttpServletResponse response, HttpServletRequest request) {
		return "login";
	}
	
	@RequestMapping("/hqgd/login")
	@ResponseBody
	public Map<String, Object> userLogin(HttpServletResponse response, HttpServletRequest request) throws Exception{
		Map<String, Object> result = loginService.login(request, response);
		return result;
	}

	@GetMapping(value = "/updatePassword")
	public String updatePassword() {
		return "user/updatePassword";
	}

	@PostMapping(value = "/updatePassword")
	@ResponseBody
	public Map<String, Object> updatePassword(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("newPassword") String newPassword) {

		Map<String, Object> result = new HashMap<>();
		result = loginService.updatePassword(username, password, newPassword);
		result.put("result", true);
		result.put("message", "ok");

		// 返回修改成功
		return result;
	}

}
