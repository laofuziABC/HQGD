package com.hqgd.pms.service.equipment.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hqgd.pms.common.CommonUtil;
import com.hqgd.pms.common.HttpProtocolHandler;
import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.domain.GeoCode;
import com.hqgd.pms.domain.GeoCodeInfo;
import com.hqgd.pms.domain.User;
import com.hqgd.pms.service.equipment.IEquipmentService;

@Service
public class EquipmentService implements IEquipmentService {

	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;
	private static final String baseUrl = "https://restapi.amap.com/v3/geocode/geo?";
	private static final String key = "aae78042bf29a967036e55a46b70a34a";

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
			String address = equipmentInfo.getAddress();
			String city = CommonUtil.subString(address, "省", "市");
			GeoCode geoCode = geocode(address, city);
			String latlon = geoCode.getLocation();
			String adcode = geoCode.getAdcode();
			equipmentInfo.setLngLat(latlon);
			equipmentInfo.setPostalCode(adcode);
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

	private GeoCode geocode(String address, String city) {
		String url = baseUrl + "key=" + key + "&address=" + address + "&city=" + city;
		String resultJson = HttpProtocolHandler.httpsRequest(url, "GET", null);
		Gson gson = new Gson();
		GeoCodeInfo geoCodeInfo = new GeoCodeInfo();
		geoCodeInfo = gson.fromJson(resultJson, geoCodeInfo.getClass());
		GeoCode geoCode = geoCodeInfo.getGeocodes().get(0);
		return geoCode;
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
	public List<EquipmentInfo> selectAllByUser(String userId) {
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAllEquipmentByUser(userId);
		return equipmentInfoList;
	}

	@Override
	public List<EquipmentInfo> selectAll() {
		List<EquipmentInfo> equipmentInfoList = equipmentInfoMapper.selectAll();
		return equipmentInfoList;
	}

	// @Override
	// public List<EquipmentParam> selectEquipmentParam(String equipmentId) {
	// List<EquipmentParam> equipmentParamList =
	// equipmentParamMapper.selectEquipmentParam(equipmentId);
	// return equipmentParamList;
	// }

	// @Override
	// public Boolean setEquipmentParam(EquipmentParam equipmentParam, User
	// loginUser) {
	// equipmentParam.setCreator(loginUser.getUserName());
	// equipmentParam.setCreateTime(new Date());
	// equipmentParam.setUpdater(loginUser.getUserName());
	// equipmentParam.setUpdateTime(new Date());
	// int i = equipmentParamMapper.setEquipmentParam(equipmentParam);
	// Boolean result = (i == 0) ? false : true;
	// // Map<String, Object> resultMap = new HashMap<String, Object>();
	// // resultMap.put("success", result);
	// // resultMap.put("resultCode", "00000007");
	// // resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
	// // resultMap.put("message", "");
	// return result;
	// }

	// @Override
	// public Map<String, Object> updateParam(EquipmentParam equipmentParam, User
	// loginUser) {
	// equipmentParam.setUpdater(loginUser.getUserName());
	// equipmentParam.setUpdateTime(new Date());
	// int i = equipmentParamMapper.updateParam(equipmentParam);
	// Boolean result = (i == 0) ? false : true;
	// Map<String, Object> resultMap = new HashMap<String, Object>();
	// resultMap.put("success", result);
	// resultMap.put("resultCode", "00000008");
	// resultMap.put("time", CommonUtil.getSimpleFormatTimestamp());
	// resultMap.put("message", "");
	// return resultMap;
	// }

}
