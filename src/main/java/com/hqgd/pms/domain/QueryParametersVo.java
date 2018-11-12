package com.hqgd.pms.domain;

import lombok.Data;

@Data
public class QueryParametersVo {
	private Integer equipmentId;//代表设备的唯一性
    private String startTime;
    private String endTime;
	
}