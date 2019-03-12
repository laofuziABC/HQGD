package com.hqgd.pms.service.system.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.system.SysParamMapper;
import com.hqgd.pms.domain.SysParam;
import com.hqgd.pms.service.system.ISystemService;

@Service
public class SystemService implements ISystemService {
	@Resource
	private SysParamMapper sysParamMapper;

	@Override
	public Map<String, Object> setSysParam(String paramCode, String paramValue) throws IOException {
		Map<String, Object> param = new HashMap<>();
		param.put("paramCode", paramCode);
		param.put("paramValue", paramValue);
		int i = sysParamMapper.setSysParam(param);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = (i == 0) ? false : true;
		resultMap.put("message", (result) ? "设置参数成功" : "设置参数失败");
		if (result && paramCode.equals("TIME_INTERVAL")) {
			String stop = "http://localhost:7070/timer/stop";
			String start = "http://localhost:7070/timer/start";
			connectUrl(stop);
			connectUrl(start);
		}
		return resultMap;
	}

	private void connectUrl(String inurl)
			throws MalformedURLException, ProtocolException, IOException, UnsupportedEncodingException {
		URL url = new URL(inurl);
		// 将url 以 open方法返回的urlConnection 连接强转为HttpURLConnection连接 (标识一个url所引用的远程对象连接)
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 此时cnnection只是为一个连接对象,待连接中
			// 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
		connection.setDoOutput(true);
		// 设置连接输入流为true
		connection.setDoInput(true);
		// 设置请求方式为post
		connection.setRequestMethod("POST");
		// post请求缓存设为false
		connection.setUseCaches(false);
		// 设置该HttpURLConnection实例是否自动执行重定向
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("accept", "application/xml");
		// 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
		connection.connect();
		DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
		dataout.writeBytes("");
		// 输出完成后刷新并关闭流
		dataout.flush();
		dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
		// 连接发起请求,处理服务器响应 (从连接获取到输入流并包装为bufferedReader)
		BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String line;
		StringBuilder sb = new StringBuilder(); // 用来存储响应数据
		// 循环读取流,若不到结尾处
		while ((line = bf.readLine()) != null) {
			sb.append(line).append(System.getProperty("line.separator"));
		}
		bf.close(); // 重要且易忽略步骤 (关闭流,切记!)
		connection.disconnect(); // 销毁连接
	}

	@Override
	public List<SysParam> selectSysParam() {
		List<SysParam> sysParam = sysParamMapper.selectSysParam();
		return sysParam;
	}

}
