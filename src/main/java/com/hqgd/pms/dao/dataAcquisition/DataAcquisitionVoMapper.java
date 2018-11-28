package com.hqgd.pms.dao.dataAcquisition;

import java.util.List;
import java.util.Map;

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

	int selectTotalChNum();

	int selectEquipCh(String equipmentId);

	Integer selectTotal(Map<String, Object> param);
}