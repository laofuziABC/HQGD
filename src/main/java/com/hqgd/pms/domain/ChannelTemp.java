package com.hqgd.pms.domain;

public class ChannelTemp {
    private String channelNum;

    private String tem;

    private String receiveTime;

    public String getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(String channelNum) {
        this.channelNum = channelNum == null ? null : channelNum.trim();
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem == null ? null : tem.trim();
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime == null ? null : receiveTime.trim();
    }
}