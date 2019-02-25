package com.hqgd.pms.dao.equipment;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.EquipmentInfo;

public interface EquipmentInfoMapper {
	int deleteByPrimaryKey(String equipmentId);

	int insert(EquipmentInfo record);

	int insertSelective(EquipmentInfo record);

	EquipmentInfo selectByPrimaryKey(String equipmentId);

	int updateByPrimaryKeySelective(EquipmentInfo record);

	int updateByPrimaryKey(EquipmentInfo record);

	List<EquipmentInfo> selectAll();

	List<EquipmentInfo> selectLngLat();

	List<EquipmentInfo> selectLngLatByUser(String userName);

	List<EquipmentInfo> selectAllByParam(Map<String, String> param);
	
	List<String> selectFrame();

	String selectTypeById(String equipmentId);

	EquipmentInfo selectByHbid(Map<String, String> param);

}