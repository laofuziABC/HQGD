package com.hqgd.pms.hbase;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "hbase")
public class HbaseProperties {
	private Map<String, String> config;

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
}
