/**
 * 设置静态资源的加载路径
 * 默认将静态资源置于resource的static文件夹下
 * @author laofuzi 2018.11..14
 */
public class MyConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
