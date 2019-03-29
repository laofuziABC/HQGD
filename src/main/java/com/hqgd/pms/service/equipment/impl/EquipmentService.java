package com.hqgd.pms.service.equipment.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IEquipmentService;

@Service
public class EquipmentService implements IEquipmentService {

	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

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
	public Map<String, Object> update(EquipmentInfo equipmentInfo, User loginUser, String add) {
		equipmentInfo.setUpdater(loginUser.getUserName());
		equipmentInfo.setUpdateTime(CommonUtil.getSimpleFormatTimestamp());
		Map<String, Object> resultMap = new HashMap<>();
		String adcode = equipmentInfo.getAdcode();
		equipmentInfo.setCadcode(adcode.substring(0, 4));
		equipmentInfo.setPadcode(adcode.substring(0, 2));
		Boolean result = false;
		if (Boolean.valueOf(add)) {
			EquipmentInfo equipFind = equipmentInfoMapper.selectByPrimaryKey(equipmentInfo.getEquipmentId());
			if (equipFind == null) {
				equipmentInfo.setCreator(loginUser.getUserName());
				equipmentInfo.setCreateTime(CommonUtil.getSimpleFormatTimestamp());
				int i = equipmentInfoMapper.insert(equipmentInfo);
				result = (i == 0) ? false : true;
				resultMap.put("message", (result) ? "添加设备成功" : "添加设备失败");
			} else {
				resultMap.put("message", "该设备ID已经存在");
			}
		} else {
			int i = equipmentInfoMapper.updateByPrimaryKeySelective(equipmentInfo);
			result = (i == 0) ? false : true;
			resultMap.put("message", (result) ? "更新设备成功" : "更新设备失败");
		}
		resultMap.put("success", result);
		resultMap.put("resultCode", "00000003");
		resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
		return resultMap;
	}

	@Override
	public EquipmentInfo select(String equipmentId) {
		EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByPrimaryKey(equipmentId);
		return equipmentInfo;
	}

	@Override
	public List<EquipmentInfo> selectAll() {
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAll();
		return equipmentInfoList;
	}

	@Override
	public List<EquipmentInfo> selectAllByParam(HttpServletRequest request) {
		Map<String, String> param = new HashMap<>();
		param.put("userName", request.getParameter("userName"));
		param.put("adcode", request.getParameter("adcode"));
		if(request.getParameter("type")==null||request.getParameter("type").equals("0")) {
			param.put("type", null);
		}else {
			param.put("type", request.getParameter("type"));
		}
		if(request.getParameter("equipmentName")==null||request.getParameter("equipmentName")=="") {
			param.put("equipmentName", null);
		}else {
			param.put("equipmentName", request.getParameter("equipmentName"));
		}
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAllByParam(param);
		return equipmentInfoList;
	}
	@Override
	public EquipmentInfo selectByHbid(Map<String, String> param) {
		EquipmentInfo equipmentInfo = equipmentInfoMapper.selectByHbid(param);
		return equipmentInfo;
	}

	public List<Map<String, String>> selectAllByHb(String heartbeat) {
		List<Map<String, String>> equipmentInfoList = equipmentInfoMapper.selectAllByHb(heartbeat);
		return equipmentInfoList;
	}

}
