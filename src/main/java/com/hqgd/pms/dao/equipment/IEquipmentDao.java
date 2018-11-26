package com.hqgd.pms.dao.equipment;

import java.util.List;

import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.EquipmentParam;

public interface IEquipmentDao {

	List<EquipmentInfo> select(String param);

	List<EquipmentParam> selectEquipmentParam(String equipmentId);

	int setEquipmentParam(EquipmentParam equipmentParam);

}
