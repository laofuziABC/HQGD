package com.hqgd.pms.dao.equipment;

import java.util.List;

import com.hqgd.pms.domain.EquipmentParam;

public interface EquipmentParamMapper {
	int insert(EquipmentParam record);

    int insertSelective(EquipmentParam record);
    
	List<EquipmentParam> selectEquipmentParam(String equipmentId);

	int setEquipmentParam(EquipmentParam equipmentParam);

	int updateParam(EquipmentParam equipmentParam);
}