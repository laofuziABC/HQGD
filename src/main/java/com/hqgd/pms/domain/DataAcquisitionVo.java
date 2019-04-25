package com.hqgd.pms.domain;

import java.util.Date;

//@Data
public class DataAcquisitionVo implements Comparable<DataAcquisitionVo> {
	private Integer id;

	private String equipmentId;

	private String equipmentName;

	private String address;

	private String channelNum;

	private String maxTem;

	private String minTem;

	private String opticalFiberPosition;

	private String temperature;

	private String pd;

	private String uv;

	private int state;

	private String message;

	private Date receiveTime;

	private String dutyPerson;

	private String tel;

	private Integer total;

	private Integer numOfCh;

	public Integer getNumOfCh() {
		return numOfCh;
	}

	public void setNumOfCh(Integer numOfCh) {
		this.numOfCh = numOfCh;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(String channelNum) {
		this.channelNum = channelNum;
	}

	public String getMaxTem() {
		return maxTem;
	}

	public void setMaxTem(String maxTem) {
		this.maxTem = maxTem;
	}

	public String getMinTem() {
		return minTem;
	}

	public void setMinTem(String minTem) {
		this.minTem = minTem;
	}

	public String getOpticalFiberPosition() {
		return opticalFiberPosition;
	}

	public void setOpticalFiberPosition(String opticalFiberPosition) {
		this.opticalFiberPosition = opticalFiberPosition;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getPd() {
		return pd;
	}

	public void setPd(String pd) {
		this.pd = pd;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDutyPerson() {
		return dutyPerson;
	}

	public void setDutyPerson(String dutyPerson) {
		this.dutyPerson = dutyPerson;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public int compareTo(DataAcquisitionVo d) {
		return d.getReceiveTime().compareTo(receiveTime);
	}
}
