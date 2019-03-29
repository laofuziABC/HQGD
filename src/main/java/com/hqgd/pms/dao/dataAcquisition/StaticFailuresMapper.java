package com.hqgd.pms.dao.dataAcquisition;

import java.util.List;
import java.util.Map;

import com.hqgd.pms.domain.StaticFailures;

public interface StaticFailuresMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StaticFailures record);

    int insertSelective(StaticFailures record);

    StaticFailures selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaticFailures record);

    int updateByPrimaryKey(StaticFailures record);

	List<StaticFailures> errorStateStatic(Map<String, Object> param);

	int selectByDate(String startTime);
}