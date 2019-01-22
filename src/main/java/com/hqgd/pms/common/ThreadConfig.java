package com.hqgd.pms.common;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 配置线程池
 * @author laofuzi
 *	2019.01.22
 */
@Configuration
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {
	
	/**
	 * 配置线程池的相关配置项
	 * 设定核心线程数为5
	 * 最大线程数为10
	 * 缓冲队列容量为10
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(10);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.setThreadNamePrefix("开启MyAsync-");
		executor.initialize();
		return executor;
	}
	
	/**
	 * 处理线程异常
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
	

}
