package com.hqgd.pms.dao.dataAcquisition;

import java.util.List;

import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;

public interface IDataAcquisitionDao {

	List<DataAcquisitionVo> getRealTimeDate(String equipmentId);

	List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo);

	List<DataAcquisitionVo> historicalCurve(QueryParametersVo queryVo);

}
