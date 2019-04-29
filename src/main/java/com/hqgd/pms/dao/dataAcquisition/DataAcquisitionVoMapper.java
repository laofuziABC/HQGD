package com.hqgd.pms.dao.dataAcquisition;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.ChannelExtremum;
import com.hqgd.pms.domain.DataAcquisitionVo;

public interface DataAcquisitionVoMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(DataAcquisitionVo record);

	int insertSelective(DataAcquisitionVo record);

	DataAcquisitionVo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(DataAcquisitionVo record);

	int updateByPrimaryKey(DataAcquisitionVo record);

	List<DataAcquisitionVo> selectRealTimeDataById(String equipmentId);

	List<DataAcquisitionVo> selectHistoricalDataById(Map<String, Object> param);

	List<DataAcquisitionVo> selectHistoricalCurveById(Map<String, Object> param);

	Integer selectTotal(Map<String, Object> param);

	List<DataAcquisitionVo> recordExport(Map<String, Object> param);

	List<Date> selectAllTimestamp(Map<String, Object> param);

	List<String> selectAllChannels(Map<String, Object> param);

	List<DataAcquisitionVo> selectChanDataByParam(Map<String, Object> param);

	List<DataAcquisitionVo> selectAllEquipRealtime(String userName);

	void deleteFirst(String lastTime);

	void insertLast();

	List<DataAcquisitionVo> selectRealTimeDataById(Map<String, Object> param);

	void deleter(Map<String, Object> param);

	void insertt(Map<String, Object> param);

	void insert1(DataAcquisitionVo d);

	void insert2(DataAcquisitionVo d);

	void insert3(DataAcquisitionVo d);

	void insert4(DataAcquisitionVo d);

	void insertr1(DataAcquisitionVo d);

	void insertr2(DataAcquisitionVo d);

	void insertr3(DataAcquisitionVo d);

	void insertr4(DataAcquisitionVo d);

	List<DataAcquisitionVo> selectAllFailures(Map<String, Object> param);

	DataAcquisitionVo selectYesDayFailures(Map<String, String> param1);

	List<DataAcquisitionVo> selectTodayFirFailures(Map<String, Object> param);

	List<DataAcquisitionVo> selectEqu(Map<String, Object> param);

	List<ChannelExtremum> findChannelExtremums(Map<String, Object> param);

	List<DataAcquisitionVo> selectCurveById(Map<String, Object> param);

}