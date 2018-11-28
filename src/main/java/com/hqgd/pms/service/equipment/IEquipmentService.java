package com.hqgd.pms.service.equipment;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.EquipmentParam;
import com.hqgd.pms.domain.User;

public interface IEquipmentService {

	Map<String, Object> add(EquipmentInfo equipmentInfo, User userLog);

	Map<String, Object> delete(String equipmentId);

	Map<String, Object> update(EquipmentInfo equipmentInfo, User loginUser);

	EquipmentInfo select(String equipmentId);

	List<EquipmentInfo> selectAll(String userId);

	List<EquipmentParam> selectEquipmentParam(String equipmentId);

	Boolean setEquipmentParam(EquipmentParam equipmentParam, User loginUser);

	Map<String, Object> updateParam(EquipmentParam equipmentParam, User loginUser);

}
