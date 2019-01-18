package com.hqgd.pms.service.map;

import java.util.List;

import com.hqgd.pms.domain.EquipmentInfo;

public interface IMapService {

	List<EquipmentInfo> getLonLats(String roleId, String userName);

}
