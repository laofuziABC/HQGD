package com.hqgd.pms.domain;

public class ChannelExtremum {
	private String channel;		//通道号
	private Float max;				//通道温度最大值
	private Float min;				//通道温度最小值
	
	public ChannelExtremum() {}
	public ChannelExtremum(String channel, Float max, Float min) {
		this.channel=channel;
		this.max=max;
		this.min=min;
	}
	
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Float getMax() {
		return max;
	}
	public void setMax(Float max) {
		this.max = max;
	}
	public Float getMin() {
		return min;
	}
	public void setMin(Float min) {
		this.min = min;
	}
}
