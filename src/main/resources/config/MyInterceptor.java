/**
 * 配置拦截器
 * 实现HandlerInterceptor接口
 * 重写preHandle、postHandle、afterCompletion方法
 * @author laofuzi 2018.11.27
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hqgd.pms.domain.User;

@Component
public class MyInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // TODO Auto-generated method stub
		System.out.println("拦截前");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("/hqgd");
            return false;
        }
        return true;
    }
	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
		System.out.println("拦截请求");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub
    	System.out.println("拦截后");
    }

}
