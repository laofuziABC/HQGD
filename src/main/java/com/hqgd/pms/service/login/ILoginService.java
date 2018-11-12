package com.hqgd.pms.service.login;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ILoginService {

	Map<String, Object> updatePassword(String username, String password, String newPassword);

	Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
