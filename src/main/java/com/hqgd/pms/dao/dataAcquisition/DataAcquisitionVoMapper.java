package com.hqgd.pms.dao.dataAcquisition;

import com.hqgd.pms.domain.DataAcquisitionVo;

public interface DataAcquisitionVoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DataAcquisitionVo record);

    int insertSelective(DataAcquisitionVo record);

    DataAcquisitionVo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataAcquisitionVo record);

    int updateByPrimaryKey(DataAcquisitionVo record);
}