/**
 * 设置静态资源的加载路径
 * 默认将静态资源置于resource的static文件夹下
 * @author laofuzi 2018.11.14
 */

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootConfiguration
public class MyConfig extends WebMvcConfigurerAdapter {
	private MyInterceptor myInterceptor;
	//设置静态资源加载路径
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
	
	//配置拦截器的拦截规则
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns 用于添加拦截规则
        //excludePathPatterns 用于排除拦截
        registry.addInterceptor(myInterceptor).addPathPatterns("/**")
            .excludePathPatterns("/hqgd")					//登录页
            .excludePathPatterns("/hqgd/login");		//用户登录
        super.addInterceptors(registry);
    }
}
