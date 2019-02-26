package com.hqgd.pms.service.equipment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.equipment.RouterInfoMapper;
import com.hqgd.pms.domain.RouterInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IRouterService;

/**
 * 描述： 作者：姚绒 日期：2018年11月5日 上午9:43:08
 *
 */
@Service
public class RouterService implements IRouterService {

	@Resource
	private RouterInfoMapper routerInfoMapper;

	@Override
	public Map<String, Object> delete(String id) {
		Map<String, Object> map = new HashMap<>();
		int i = routerInfoMapper.deleteByPrimaryKey(id);
		Boolean result = (i == 0) ? false : true;
		map.put("success", result);
		return map;
	}

	@Override
	public List<RouterInfo> selectByRouterName(String routerName) {
		List<RouterInfo> routerInfo = routerInfoMapper.selectByRouterName(routerName);
		return routerInfo;
	}

	@Override
	public List<RouterInfo> selectAll() {
		List<RouterInfo> routerInfo = routerInfoMapper.selectAll();
		return routerInfo;
	}

	@Override
	public Map<String, Object> add(RouterInfo routerInfo, User loginUser) {
		Boolean result = false;
		String message = "";
		String routerId = routerInfo.getRouterId();
		RouterInfo routerfind = routerInfoMapper.selectByPrimaryKey(routerId);
		if (routerfind == null) {
			routerInfo.setUpdater(loginUser.getUserName());
			routerInfo.setCreator(loginUser.getUserName());
			routerInfo.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
			routerInfo.setCreateTime(CommonUtil.getSimpleFormatTimestamp());
			int i = routerInfoMapper.insert(routerInfo);
			result = (i == 0) ? false : true;
			message = (result == true) ? "添加路由器成功！" : "添加路由器失败！";
		} else {
			result = false;
			message = "该路由器Id已经存在！";
		}

		Map<String, Object> map = new HashMap<>();
		map.put("success", result);
		map.put("message", message);
		return map;
	}

	@Override
	public Map<String, Object> update(RouterInfo routerInfo, User loginUser) {
		routerInfo.setUpdater(loginUser.getUserName());
		routerInfo.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
		Map<String, Object> map = new HashMap<>();
		int i = routerInfoMapper.updateByPrimaryKeySelective(routerInfo);
		Boolean result = (i == 0) ? false : true;
		map.put("success", result);
		return map;
	}

}
