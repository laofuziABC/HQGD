package com.hqgd.pms.service.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILoginService {

	Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
