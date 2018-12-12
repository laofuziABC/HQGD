package com.hqgd.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@SpringBootApplication
@MapperScan("com.hqgd.pms.dao")
public class HqgdPmsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HqgdPmsServerApplication.class, args);
	}
}
