package com.hqgd.pms.service.system;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.SysParam;

public interface ISystemService {

	Map<String, Object> setSysParam(String paramCode, String paramValue) throws MalformedURLException, IOException;

	List<SysParam> selectSysParam();

}
