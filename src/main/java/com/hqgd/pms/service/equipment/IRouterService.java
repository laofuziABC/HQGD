package com.hqgd.pms.service.equipment;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.RouterInfo;
import com.hqgd.pms.domain.User;

public interface IRouterService {

	List<RouterInfo> selectByRouterName(String routerName);

	List<RouterInfo> selectAll();

	Map<String, Object> add(RouterInfo routerInfo, User userLog);

	Map<String, Object> update(RouterInfo routerInfo, User userLog);

	Map<String, Object> delete(String routerId);}
