package com.hqgd.pms.dao.equipment;

import java.util.List;

import com.hqgd.pms.domain.EquipmentInfo;

public interface EquipmentInfoMapper {
	int deleteByPrimaryKey(String equipmentId);

	int insert(EquipmentInfo record);

	int insertSelective(EquipmentInfo record);

	EquipmentInfo selectByPrimaryKey(String equipmentId);

	int updateByPrimaryKeySelective(EquipmentInfo record);

	int updateByPrimaryKey(EquipmentInfo record);

	List<EquipmentInfo> selectAllEquipmentByUser(String userName);

	List<EquipmentInfo> selectAll();

	int selectTotalChNum();

	int selectEquipCh(String equipmentId);

	List<String> selectLngLat();

	List<EquipmentInfo> selectAllEquipmentByAddress(String adcode);

	EquipmentInfo selectByEquipmentName(String equipmentName);

	List<EquipmentInfo> selectAllByType(String type);

}