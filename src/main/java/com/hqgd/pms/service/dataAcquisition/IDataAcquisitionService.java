package com.hqgd.pms.service.dataAcquisition;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.domain.StaticFailures;

public interface IDataAcquisitionService {

	Map<String, Object> getHistoricalData(QueryParametersVo queryVo) throws IOException;

	List<DataAcquisitionVo> allEquipRealtime(String userName, String roleId);

	List<DataAcquisitionVo> record(QueryParametersVo data);

	List<StaticFailures> errorStateStatic(QueryParametersVo queryVo);

	Map<String, Object> getHistoryExtremums(QueryParametersVo queryVo);

	void insertHbase(DataAcquisitionVo d) throws IOException;

	Map<String, Object> realTimeCurve(QueryParametersVo queryVo) throws ParseException;

	Map<String, Object> historicalCurve(QueryParametersVo queryVo) throws ParseException;

	List<DataAcquisitionVo> parseReceivedData(String inputString, String url) throws IOException;;

}
