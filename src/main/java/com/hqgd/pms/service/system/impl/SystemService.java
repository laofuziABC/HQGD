package com.hqgd.pms.service.system.impl;

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
	public Map<String, Object> setSysParam(String paramCode, String paramValue) {
		Map<String, Object> param = new HashMap<>();
		param.put("paramCode", paramCode);
		param.put("paramValue", paramValue);
		int i = sysParamMapper.setSysParam(param);
		Map<String, Object> resultMap = new HashMap<>();
		boolean result = (i == 0) ? false : true;
		resultMap.put("message", (result) ? "设置参数成功" : "设置参数失败");
		return resultMap;
	}

	@Override
	public List<SysParam> selectSysParam() {
		List<SysParam> sysParam = sysParamMapper.selectSysParam();
		return sysParam;
	}

}
