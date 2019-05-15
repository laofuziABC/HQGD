package com.hqgd.pms.testlaofuzi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.dataAcquisition.DataAcquisitionVoMapper;
import com.hqgd.pms.dao.equipment.EquipmentInfoMapper;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.EquipmentInfo;

@Service
public class MonitorServiceImpl implements MonitorService {
	@Resource
	private EquipmentInfoMapper equipmentInfoMapper;
	@Resource
	private DataAcquisitionVoMapper dataAcquisitionVoMapper;

	/**
	 * 根据查询条件查询设备信息和设备最新温度监测信息
	 * return 设备信息、设备通道温度上下限和位置信息、最新通道温度
	 */
	@Override
	public List<Map<String, Object>> getInfosList(HttpServletRequest request) {
		//使用Map集合组装查询参数
		Map<String, String> query = new HashMap<String, String>();
		String type = request.getParameter("type");
		switch (type) {
			case "1": query.put("table", "hq_equipment_monitor_data_r1"); break;
			case "2": query.put("table", "hq_equipment_monitor_data_r2"); break;
			case "3": query.put("table", "hq_equipment_monitor_data_r3"); break;
			case "4": query.put("table", "hq_equipment_monitor_data_r4"); break;
		}
		query.put("userName", request.getParameter("userName"));
		query.put("adcode", String.valueOf(request.getAttribute("adcode")));
		query.put("type", type);
		//查询结果并以List集合封装
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<EquipmentInfo> equiList = equipmentInfoMapper.selectAllByParam(query);
		if(equiList.size()>0) {
			for(int i=0; i<equiList.size(); i++) {
				EquipmentInfo equipment = equiList.get(i);
				List<DataAcquisitionVo> dateList = new ArrayList<DataAcquisitionVo>();
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("equipmentId", equipment.getEquipmentId());
				String etype = equipment.getType();
				switch (etype) {
					case "1": param.put("table", "hq_equipment_monitor_data_r1"); break;
					case "2": param.put("table", "hq_equipment_monitor_data_r2"); break;
					case "3": param.put("table", "hq_equipment_monitor_data_r3"); break;
					case "4": param.put("table", "hq_equipment_monitor_data_r4"); break;
				}
				dateList = dataAcquisitionVoMapper.selectRealTimeDataById(param);
				Map<String, Object> element = new HashMap<String, Object>();
				element.put("equipment", equipment);
				element.put("dateList", dateList);
				result.add(element);
			}
		}
		return result;
	}

}
