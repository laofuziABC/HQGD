package com.hqgd.pms.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器拦截规则
 * 需要注意的是SpringBoot 2.0以前，是通过继承WebMvcConfigurerAdapter类
 * 但在SpringBoot 2.0之后，需要通过实现WebMvcConfigurer接口。
 * @author laofuzi 2018.11.28
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	/**
	 * 先注册组件
	 * 将自定义的拦截器注入到配置中
	 * @return
	 */
	@Bean
	MyInterceptor interceptor() {
		return new MyInterceptor();
	}
	
	/**
	 * 配置拦截器的拦截规则
	 * addPathPatterns ——添加拦截规则
	 * excludePathPatterns ——排除拦截
	 * 需要注意的是，在配置拦截规则时，
	 * 		“/*”		——表示的是当前路径下的一级url地址
	 * 		“/**”	——表示的是当前目录下的所有层级url地址(甚至静态资源)
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {}

}
