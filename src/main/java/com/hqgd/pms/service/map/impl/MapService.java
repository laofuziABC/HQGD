package com.hqgd.pms.service.map.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.map.IMapService;

@Service
public class MapService implements IMapService {
	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;

	@Override
	public List<EquipmentInfo> getLonLats() {
		return equipmentInfoMapper.selectLngLat();
	}

}
