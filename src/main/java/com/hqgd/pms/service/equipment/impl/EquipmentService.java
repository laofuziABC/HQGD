package com.hqgd.pms.service.equipment.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.dao.equipment.IEquipmentDao;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.EquipmentParam;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IEquipmentService;

@Service
public class EquipmentService implements IEquipmentService {

	@Autowired
	IEquipmentDao equipmentDao;

	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

	@Override
	public Map<String, Object> add(EquipmentInfo equipmentInfo, User loginUser) {
		Map<String, Object> resultMap = new HashMap<>();
		Boolean result = false;
		EquipmentInfo equipFind = equipmentInfoMapper.selectByPrimaryKey(equipmentInfo.getEquipmentId());
		if (equipFind == null) {
			equipmentInfo.setUpdater(loginUser.getUserName());
			equipmentInfo.setCreator(loginUser.getUserName());
			equipmentInfo.setUpdateTime(new Date());
			equipmentInfo.setCreateTime(new Date());

			int i = equipmentInfoMapper.insert(equipmentInfo);
			result = (i == 0) ? false : true;
			resultMap.put("message", "添加设备成功");
		} else {
			resultMap.put("message", "该设备ID已经存在");
		}
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000001");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		return resultMap;
	}

	@Override
	public Map<String, Object> delete(String equipmentId) {
		Map<String, Object> resultMap = new HashMap<>();
		int i = equipmentInfoMapper.deleteByPrimaryKey(equipmentId);
		Boolean result = (i == 0) ? false : true;
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000002");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		return resultMap;
	}

	@Override
	public Map<String, Object> update(EquipmentInfo equipmentInfo, User loginUser) {
		equipmentInfo.setUpdater(loginUser.getUserName());
		equipmentInfo.setUpdateTime(new Date());
		Map<String, Object> resultMap = new HashMap<>();
		int i = equipmentInfoMapper.updateByPrimaryKeySelective(equipmentInfo);
		Boolean result = (i == 0) ? false : true;
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000003");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		return resultMap;
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

	@Override
	public List<EquipmentParam> selectEquipmentParam(String equipmentId) {
		List<EquipmentParam> equipmentParamList = equipmentDao.selectEquipmentParam(equipmentId);
		return equipmentParamList;
	}

	@Override
	public Map<String, Object> setEquipmentParam(EquipmentParam equipmentParam, User loginUser) {
		equipmentParam.setUpdater(loginUser.getUserName());
		equipmentParam.setUpdateTime(new Date());
		int i = equipmentDao.setEquipmentParam(equipmentParam);
		Boolean result = (i == 0) ? false : true;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000007");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		resultMap.put("message", "");
		return resultMap;
	}

}
