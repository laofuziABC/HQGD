package com.hqgd.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.hqgd.pms.dao")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class HqgdPmsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HqgdPmsServerApplication.class, args);
	}
}
