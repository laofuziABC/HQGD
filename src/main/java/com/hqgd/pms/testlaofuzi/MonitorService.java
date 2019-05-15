package com.hqgd.pms.testlaofuzi;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface MonitorService {

	List<Map<String, Object>> getInfosList(HttpServletRequest request);

}
