package com.hqgd.pms.service.map.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.EquipmentInfo;
import com.hqgd.pms.service.equipment.IEquipmentService;
import com.hqgd.pms.service.map.IMapService;

@Service
public class MapService implements IMapService {
	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private IEquipmentService equipmentService;

	@Override
	public List<EquipmentInfo> getLonLats(HttpServletRequest request) {
		String roleId = request.getParameter("roleId");
		if (roleId.equals("1")) {
			return equipmentInfoMapper.selectLngLat();
		} else {
			return equipmentService.selectAllByParam(request);
		}
	}

}
