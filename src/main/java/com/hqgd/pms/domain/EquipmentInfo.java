package com.hqgd.pms.domain;

import java.util.Date;

public class EquipmentInfo {
    private String equipmentId;

    private String equipmentName;

    private String userName;

    private String userId;

    private String frameStru;

    private String icon;

    private String channelTem;

    private String address;

    private String lngLat;

    private String postalCode;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;
    
    

    public String getLngLat() {
		return lngLat;
	}

	public void setLngLat(String lngLat) {
		this.lngLat = lngLat;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getFrameStru() {
        return frameStru;
    }

    public void setFrameStru(String frameStru) {
        this.frameStru = frameStru == null ? null : frameStru.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public String getChannelTem() {
        return channelTem;
    }

    public void setChannelTem(String channelTem) {
        this.channelTem = channelTem == null ? null : channelTem.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode == null ? null : postalCode.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater == null ? null : updater.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}