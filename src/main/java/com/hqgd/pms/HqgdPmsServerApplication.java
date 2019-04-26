package com.hqgd.pms;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hqgd.pms.tcp.SystemStartServlet;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@MapperScan("com.hqgd.pms.dao")
public class HqgdPmsServerApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HqgdPmsServerApplication.class, args);
		//启动socket
		SystemStartServlet server = new SystemStartServlet();
		server.init();
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 防止客户端被分配访问未被清理临时目录的服务器时不能正常上传文件
		String tmpPath = "D:/工具软件/";
		File file = new File(tmpPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		factory.setLocation(tmpPath);
		return factory.createMultipartConfig();
	}

}
