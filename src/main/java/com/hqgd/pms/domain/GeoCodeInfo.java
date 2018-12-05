package com.hqgd.pms.domain;

import java.util.List;

public class GeoCodeInfo {
	private String status;
	private String info;
	private String infocode;
	private String count;
	private List<GeoCode> geocodes;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfocode() {
		return infocode;
	}
	public void setInfocode(String infocode) {
		this.infocode = infocode;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<GeoCode> getGeocodes() {
		return geocodes;
	}
	public void setGeocodes(List<GeoCode> geocodes) {
		this.geocodes = geocodes;
	}
	
}


