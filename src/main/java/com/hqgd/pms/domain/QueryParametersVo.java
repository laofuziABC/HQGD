package com.hqgd.pms.domain;

//import lombok.Data;

//@Data
public class QueryParametersVo {
	private Integer equipmentId;//代表设备的唯一性
    private String startTime;
    private String endTime;
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
    
}