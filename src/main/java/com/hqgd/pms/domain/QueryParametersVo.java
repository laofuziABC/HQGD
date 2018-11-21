package com.hqgd.pms.domain;

public class QueryParametersVo {
	private String equipmentId;//代表设备的唯一性
    private String startTime;
    private String endTime;
    private String limit;
    
	public String getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(String equipmentId) {
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