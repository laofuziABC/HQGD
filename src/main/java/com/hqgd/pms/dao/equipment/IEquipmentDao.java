package com.hqgd.pms.dao.equipment;

import java.util.List;

import com.hqgd.pms.domain.EquipmentInfo;

public interface IEquipmentDao {

	List<EquipmentInfo> select(String param);

}
