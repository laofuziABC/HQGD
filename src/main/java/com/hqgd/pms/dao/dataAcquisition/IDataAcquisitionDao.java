package com.hqgd.pms.dao.dataAcquisition;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;

public interface IDataAcquisitionDao {

	List<DataAcquisitionVo> getRealTimeDate(String equipmentId);

	List<DataAcquisitionVo> getHistoricalData(Map<String, Object> param);

	List<DataAcquisitionVo> historicalCurve(QueryParametersVo queryVo);

	int getTotalChNum();

	int selectEquipCh(String equipmentId);

	Integer selectTotal(Map<String, Object> param);

}
