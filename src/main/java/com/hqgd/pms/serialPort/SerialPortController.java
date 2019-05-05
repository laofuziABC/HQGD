package com.hqgd.pms.serialPort;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.domain.SerialPortVo;

@Controller
@RequestMapping("serialPort")
public class SerialPortController {
	@Autowired
	SerialPortService serialPortService;

	@RequestMapping("selectPorts")
	public void selectPorts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 串口列表
		List<String> mCommList = null;
		mCommList = serialPortService.findPorts();
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson((mCommList == null || mCommList.size() < 1) ? "null" : mCommList));
	}

	@RequestMapping("open")
	public void openPort(HttpServletRequest request, SerialPortVo serialPortVo, HttpServletResponse response)
			throws IOException {
		Map<String, Object> resultMap = serialPortService.openSort(serialPortVo);
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson(resultMap));
	}

	@RequestMapping("close")
	private void closePort(HttpServletRequest request, HttpServletResponse response) throws IOException {
		serialPortService.closePort();
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(new Gson().toJson("关闭成功"));
	}
}
