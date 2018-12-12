package com.hqgd.pms.controller.login;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
		return "index";
	}
	
	@RequestMapping("/hqgd/login")
	@ResponseBody
	public Map<String, Object> userLogin(HttpServletResponse response, HttpServletRequest request) throws Exception{
		Map<String, Object> result = loginService.login(request, response);
		return result;
	}

}
