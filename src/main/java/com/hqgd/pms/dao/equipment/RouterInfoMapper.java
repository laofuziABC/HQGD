package com.hqgd.pms.dao.equipment;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.Menu;
import com.hqgd.pms.domain.RouterInfo;

public interface RouterInfoMapper {
    int deleteByPrimaryKey(String routerId);

    int insert(RouterInfo record);

    int insertSelective(RouterInfo record);

    RouterInfo selectByPrimaryKey(String routerId);

    int updateByPrimaryKeySelective(RouterInfo record);

    int updateByPrimaryKey(RouterInfo record);

	List<RouterInfo> selectAll();

	List<RouterInfo> selectByRouterName(String routerName);

	List<Map<String,String>> selectConEqui();

	void updateIp(String routerId, String ip);

	void updateConnect(String routerId);
}