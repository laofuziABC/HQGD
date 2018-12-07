package com.hqgd.pms.domain;

import java.util.Date;

public class EquipmentInfo {
    private Integer id;

    private String equipmentId;

    private String equipmentName;

    private String userName;

    private String userId;

    private String channelTem;

    private String address;

    private String adcode;

    private String cadcode;

    private String padcode;

    private String lngLat;

    private String frameStru;

    private String icon;

    private String creator;

    private Date createTime;

    private String updater;

    private Date updateTime;

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

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode == null ? null : adcode.trim();
    }

    public String getCadcode() {
        return cadcode;
    }

    public void setCadcode(String cadcode) {
        this.cadcode = cadcode == null ? null : cadcode.trim();
    }

    public String getPadcode() {
        return padcode;
    }

    public void setPadcode(String padcode) {
        this.padcode = padcode == null ? null : padcode.trim();
    }

    public String getLngLat() {
        return lngLat;
    }

    public void setLngLat(String lngLat) {
        this.lngLat = lngLat == null ? null : lngLat.trim();
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