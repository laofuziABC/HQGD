package com.hqgd.pms.service.dataAcquisition;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;

public interface IDataAcquisitionService {

	List<DataAcquisitionVo> execGetRealTimeData(String equipmentId);

	List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo);

	Map<String, Object> historicalCurve(QueryParametersVo queryVo) throws Exception;


	Integer selectTotal(QueryParametersVo queryVo);

	Map<String, Object> getPeriodDataByQuery(QueryParametersVo queryVo);

	List<DataAcquisitionVo> allEquipRealtime(String userName, String roleId);

}
