package com.hqgd.pms.service.dataAcquisition.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hqgd.pms.dao.dataAcquisition.IDataAcquisitionDao;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

@Service
public class DataAcquisitionService implements IDataAcquisitionService{
	@Autowired
	@Qualifier("dataAcquisitionDao")
	private IDataAcquisitionDao dataAcquisitionDao;
	@Override
	public List<DataAcquisitionVo> execGetRealTimeData(String equipmentId) {
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionDao.getRealTimeDate(equipmentId);
		return realTimeDateList;
	}
	@Override
	public List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo) {
		List<DataAcquisitionVo> getHistoricalDataList = dataAcquisitionDao.getHistoricalData(queryVo);
		return getHistoricalDataList;
	}

}
