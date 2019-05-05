package com.hqgd.pms.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hqgd.pms.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置拦截器
 * 实现HandlerInterceptor接口
 * 重写preHandle、postHandle、afterCompletion方法
 * @author laofuzi 2018.11.27
 */
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
		
		log.info("当前请求即将处理。");
	    User user = (User) request.getSession().getAttribute("user");
	    if (user == null) {
	        response.sendRedirect("/hqgd");
	        return false;
	    }
	    return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
    	log.info("当前请求正在处理中……");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    	log.info("当前请求已完成处理。");
    }

}
