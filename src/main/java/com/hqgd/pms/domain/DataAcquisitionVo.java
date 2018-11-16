package com.hqgd.pms.domain;

import java.util.Date;

import lombok.Data;

@Data
public class DataAcquisitionVo {
	 private Integer id;

	    private String equipmentId;

	    private String equipmentName;

	    private String address;

	    private String channelNum;

	    private String opticalFiberPosition;

	    private double temperature;

	    private String pd;

	    private String uv;

	    private Integer state;

	    private String message;

	    private String receiveTime;

	    private String dutyPerson;

	    private String tel;

}
