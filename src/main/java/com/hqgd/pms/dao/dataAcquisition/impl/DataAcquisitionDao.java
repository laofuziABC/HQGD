package com.hqgd.pms.dao.dataAcquisition.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hqgd.pms.dao.dataAcquisition.IDataAcquisitionDao;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;

@Repository
public class DataAcquisitionDao implements IDataAcquisitionDao {
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<DataAcquisitionVo> getRealTimeDate(String equipmentId) {
		return sqlSessionTemplate.selectList("selectRealTimeDataById", equipmentId);
	}

	@Override
	public List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId().toString());
		param.put("startTime", queryVo.getStartTime());
		param.put("endTime", queryVo.getEndTime());
		param.put("limit", Integer.valueOf(queryVo.getLimit()));
		param.put("total", (Integer.valueOf(queryVo.getPage())-1)*Integer.valueOf(queryVo.getLimit()));
		param.put("state", queryVo.getState());
		String state = queryVo.getState();
		
		if (state.equals("0")) {
			return sqlSessionTemplate.selectList("selectHistoricalDataById", param);
		} else {
			return sqlSessionTemplate.selectList("selectAbnormalDataById", param);
		}

	}

	@Override
	public List<DataAcquisitionVo> historicalCurve(QueryParametersVo queryVo) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId().toString());
		param.put("startTime", queryVo.getStartTime());
		param.put("endTime", queryVo.getEndTime());
		return sqlSessionTemplate.selectList("selectHistoricalCurveById", param);
	}

}
