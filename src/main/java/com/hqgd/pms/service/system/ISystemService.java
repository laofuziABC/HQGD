package com.hqgd.pms.service.system;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.SysParam;

public interface ISystemService {

	Map<String, Object> setSysParam(String paramCode, String paramValue);

	List<SysParam> selectSysParam();

}
