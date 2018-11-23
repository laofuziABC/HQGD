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
	public List<DataAcquisitionVo> getHistoricalData(Map<String, Object> param) {
		return sqlSessionTemplate.selectList("selectHistoricalDataById", param);
	}

	@Override
	public List<DataAcquisitionVo> historicalCurve(QueryParametersVo queryVo) {
		Map<String, Object> param = new HashMap<>();
		param.put("equipmentId", queryVo.getEquipmentId().toString());
		param.put("startTime", queryVo.getStartTime());
		param.put("endTime", queryVo.getEndTime());
		return sqlSessionTemplate.selectList("selectHistoricalCurveById", param);
	}

	@Override
	public int getTotalChNum() {
		return sqlSessionTemplate.selectOne("selectTotalChNum");
	}

	@Override
	public int selectEquipCh(String equipmentId) {
		return sqlSessionTemplate.selectOne("selectEquipCh",equipmentId);
	}

	@Override
	public Integer selectTotal(Map<String, Object> param) {
		return sqlSessionTemplate.selectOne("selectTotal",param);
	}

}
