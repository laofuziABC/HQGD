package com.hqgd.pms.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChannelDatasUtil {
	private Date receiveTime;
	private Double CH1;
	private Double CH2;
	private Double CH3;
	private Double CH4;
	private Double CH5;
	private Double CH6;
	private Double CH7;
	private Double CH8;
	private Double CH9;
	private Double CH10;
	private Double CH11;
	private Double CH12;
	private Double CH13;
	private Double CH14;
	private Double CH15;
	private Double CH16;
	private Double CH17;
	private Double CH18;
	
	public ChannelDatasUtil() {}
	public ChannelDatasUtil(Double CH1,Double CH2,Double CH3,Double CH4,Double CH5,Double CH6,
			Double CH7,Double CH8,Double CH9,Double CH10,Double CH11,Double CH12,
			Double CH13,Double CH14,Double CH15,Double CH16,Double CH17,Double CH18, Date receiveTime) {
		this.CH1 = CH1;
		this.CH2 = CH2;
		this.CH3 = CH3;
		this.CH4 = CH4;
		this.CH5 = CH5;
		this.CH6 = CH6;
		this.CH7 = CH7;
		this.CH8 = CH8;
		this.CH9 = CH9;
		this.CH10 = CH10;
		this.CH11 = CH11;
		this.CH12 = CH12;
		this.CH13 = CH13;
		this.CH14 = CH14;
		this.CH15 = CH15;
		this.CH16 = CH16;
		this.CH17 = CH17;
		this.CH18 = CH18;
		this.receiveTime=receiveTime;
	}
	
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public Double getCH1() {
		return CH1;
	}
	public void setCH1(Double cH1) {
		this.CH1 = cH1;
	}
	public Double getCH2() {
		return CH2;
	}
	public void setCH2(Double cH2) {
		this.CH2 = cH2;
	}
	public Double getCH3() {
		return CH3;
	}
	public void setCH3(Double cH3) {
		this.CH3 = cH3;
	}
	public Double getCH4() {
		return CH4;
	}
	public void setCH4(Double cH4) {
		this.CH4 = cH4;
	}
	public Double getCH5() {
		return CH5;
	}
	public void setCH5(Double cH5) {
		this.CH5 = cH5;
	}
	public Double getCH6() {
		return CH6;
	}
	public void setCH6(Double cH6) {
		this.CH6 = cH6;
	}
	public Double getCH7() {
		return CH7;
	}
	public void setCH7(Double cH7) {
		this.CH7 = cH7;
	}
	public Double getCH8() {
		return CH8;
	}
	public void setCH8(Double cH8) {
		this.CH8 = cH8;
	}
	public Double getCH9() {
		return CH9;
	}
	public void setCH9(Double cH9) {
		this.CH9 = cH9;
	}
	public Double getCH10() {
		return CH10;
	}
	public void setCH10(Double cH10) {
		this.CH10 = cH10;
	}
	public Double getCH11() {
		return CH11;
	}
	public void setCH11(Double cH11) {
		this.CH11 = cH11;
	}
	public Double getCH12() {
		return CH12;
	}
	public void setCH12(Double cH12) {
		this.CH12 = cH12;
	}
	public Double getCH13() {
		return CH13;
	}
	public void setCH13(Double cH13) {
		this.CH13 = cH13;
	}
	public Double getCH14() {
		return CH14;
	}
	public void setCH14(Double cH14) {
		this.CH14 = cH14;
	}
	public Double getCH15() {
		return CH15;
	}
	public void setCH15(Double cH15) {
		this.CH15 = cH15;
	}
	public Double getCH16() {
		return CH16;
	}
	public void setCH16(Double cH16) {
		this.CH16 = cH16;
	}
	public Double getCH17() {
		return CH17;
	}
	public void setCH17(Double cH17) {
		this.CH17 = cH17;
	}
	public Double getCH18() {
		return CH18;
	}
	public void setCH18(Double cH18) {
		this.CH18 = cH18;
	}
	public Object[] exchangeToArray(int num) {
		List<Object> list=new ArrayList<Object>();
		list.add(CH1);
		list.add(CH2);
		list.add(CH3);
		list.add(CH4);
		list.add(CH5);
		list.add(CH6);
		list.add(CH7);
		list.add(CH8);
		list.add(CH9);
		list.add(CH10);
		list.add(CH11);
		list.add(CH12);
		list.add(CH13);
		list.add(CH14);
		list.add(CH15);
		list.add(CH16);
		list.add(CH17);
		list.add(CH18);
		list=list.subList(0, num);
		list.add(receiveTime);
		Object[] arr=list.toArray();
		return arr;
	}
}
