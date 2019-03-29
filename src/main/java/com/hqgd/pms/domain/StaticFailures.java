package com.hqgd.pms.domain;

import java.util.Date;

public class StaticFailures {
    private Integer id;

    private String equipmentId;

    private String equipmentName;

    private String address;

    private String channelNum;

    private String opticalFiberPosition;

    private Integer communicate;

    private Integer fiber;

    private Integer thermometer;

    private Integer overTemperature;

    private Date date;

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
        this.equipmentId = equipmentId == null ? null : equipmentId.trim();
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName == null ? null : equipmentName.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum == null ? null : channelNum.trim();
    }

    public String getOpticalFiberPosition() {
        return opticalFiberPosition;
    }

    public void setOpticalFiberPosition(String opticalFiberPosition) {
        this.opticalFiberPosition = opticalFiberPosition == null ? null : opticalFiberPosition.trim();
    }

    public Integer getCommunicate() {
        return communicate;
    }

    public void setCommunicate(Integer communicate) {
        this.communicate = communicate;
    }

    public Integer getFiber() {
        return fiber;
    }

    public void setFiber(Integer fiber) {
        this.fiber = fiber;
    }

    public Integer getThermometer() {
        return thermometer;
    }

    public void setThermometer(Integer thermometer) {
        this.thermometer = thermometer;
    }

    public Integer getOverTemperature() {
        return overTemperature;
    }

    public void setOverTemperature(Integer overTemperature) {
        this.overTemperature = overTemperature;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}