/**
 * 设置静态资源的加载路径
 * 默认将静态资源置于resource的static文件夹下
 * @author laofuzi 2018.11.14
 */

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class MyConfig extends WebMvcConfigurerAdapter {
	
	//设置静态资源加载路径
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/static/**").addResourceLocations("file:/D:/temp/upload/");
        super.addResourceHandlers(registry);
    }
	
}
