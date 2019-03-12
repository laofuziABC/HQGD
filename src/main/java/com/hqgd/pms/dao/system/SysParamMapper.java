package com.hqgd.pms.dao.system;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.SysParam;

public interface SysParamMapper {

	SysParam selectByPrimaryKey(String paramCode);

	List<SysParam> selectSysParam();

	int setSysParam(Map<String, Object> param);
}