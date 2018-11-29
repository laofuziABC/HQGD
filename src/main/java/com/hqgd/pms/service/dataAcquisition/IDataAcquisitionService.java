package com.hqgd.pms.service.dataAcquisition;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;

public interface IDataAcquisitionService {

	List<DataAcquisitionVo> execGetRealTimeData(String equipmentId);

	List<DataAcquisitionVo> getHistoricalData(QueryParametersVo queryVo);

	String execRecordExport(@Valid QueryParametersVo data);

	Map<String, Object> historicalCurve(QueryParametersVo queryVo) throws Exception;

	Integer selectTotal(QueryParametersVo queryVo);

}
