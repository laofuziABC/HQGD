package com.hqgd.pms.service.equipment;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.EquipmentParam;

public interface IEquipmentService {

	Map<String, Object> add(EquipmentInfo equipmentInfo);

	Map<String, Object> delete(String equipmentId);

	Map<String, Object> update(EquipmentInfo equipmentInfo);

	EquipmentInfo select(String equipmentId);

	List<EquipmentInfo> selectAll(String userId);

	List<EquipmentParam> selectEquipmentParam(String equipmentId);

}
