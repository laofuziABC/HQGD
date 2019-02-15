package com.hqgd.pms.service.equipment;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.User;

public interface IEquipmentService {

	Map<String, Object> delete(String equipmentId);

	Map<String, Object> update(EquipmentInfo equipmentInfo, User loginUser, String city);

	EquipmentInfo select(String equipmentId);

	List<EquipmentInfo> selectAll();

	List<EquipmentInfo> selectAllByParam(HttpServletRequest request);

}
