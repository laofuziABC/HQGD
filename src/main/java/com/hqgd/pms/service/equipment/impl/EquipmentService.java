package com.hqgd.pms.service.equipment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.dao.equipment.IEquipmentDao;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.equipment.IEquipmentService;

public class EquipmentService implements IEquipmentService {

	@Autowired
	IEquipmentDao equipmentDao;

	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

	@Override
	public Map<String, Object> add(EquipmentInfo equipmentInfo) {
		Map<String, Object> map = new HashMap<>();
		// String equipmentId = equipmentInfo.getEquipmentId();
		// String equipmentName = equipmentInfo.getEquipmentName();
		int i = equipmentInfoMapper.insert(equipmentInfo);
		Boolean result = (i == 0) ? false : true;
		// if (result) {
		// DataAcquisitionVo da = new DataAcquisitionVo();
		// da.setEquipmentId(equipmentId);
		// da.setEquipmentName(equipmentName);
		// int j = dataAcquisitionVoMapper.insert(da);
		// Boolean r = (j == 0) ? false : true;
		// map.put("result", r);
		// } else {
		map.put("result", result);
		// }
		return map;
	}

	@Override
	public Map<String, Object> delete(String equipmentId) {
		Map<String, Object> map = new HashMap<>();
		int i = equipmentInfoMapper.deleteByPrimaryKey(equipmentId);
		Boolean result = (i == 0) ? false : true;
		map.put("result", result);
		return map;
	}

	@Override
	public Map<String, Object> update(EquipmentInfo equipmentInfo) {
		Map<String, Object> map = new HashMap<>();
		int i = equipmentInfoMapper.updateByPrimaryKeySelective(equipmentInfo);
		Boolean result = (i == 0) ? false : true;
		map.put("result", result);
		return map;
	}

	@Override
	public EquipmentInfo select(String equipmentId) {
		EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(equipmentId);
		return equipmentInfo;
	}

	@Override
	public List<EquipmentInfo> selectAll(String param) {
		List<EquipmentInfo> equipmentInfoList = equipmentDao.select(param);
		return equipmentInfoList;
	}

}
