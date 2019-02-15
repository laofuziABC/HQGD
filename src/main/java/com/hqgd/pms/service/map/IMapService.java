package com.hqgd.pms.service.map;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hqgd.pms.domain.EquipmentInfo;

public interface IMapService {

	List<EquipmentInfo> getLonLats(HttpServletRequest request);

}
