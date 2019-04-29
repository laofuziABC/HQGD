package com.hqgd.pms.domain;

public class SerialPortVo {
	private String serialPort;// 串口号
	private String baudrate;// 波特率
	private String dataBits;// 数据位
	private String parity;// 校验值
	private String stopBits;// 停止位

	public String getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}

	public String getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(String baudrate) {
		this.baudrate = baudrate;
	}

	public String getDataBits() {
		return dataBits;
	}

	public void setDataBits(String dataBits) {
		this.dataBits = dataBits;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public String getStopBits() {
		return stopBits;
	}

	public void setStopBits(String stopBits) {
		this.stopBits = stopBits;
	}

}
