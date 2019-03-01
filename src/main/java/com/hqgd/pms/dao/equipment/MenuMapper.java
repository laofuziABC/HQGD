package com.hqgd.pms.dao.equipment;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.Menu;

public interface MenuMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Menu record);

	int insertSelective(Menu record);

	Menu selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Menu record);

	int updateByPrimaryKey(Menu record);

	List<Map<String,String>> selectMenu();
}