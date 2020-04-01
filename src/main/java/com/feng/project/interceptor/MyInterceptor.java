package com.feng.project.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyInterceptor implements HandlerInterceptor {
    //请求处理前，也就是访问Controller前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        Object loginUser = request.getSession().getAttribute("user");
        if(url.startsWith("/admin") && loginUser == null){
            response.sendRedirect("/login");
            return false;
        }
        if(url.startsWith("/job") && loginUser == null){
            response.sendRedirect("/login");
            return false;
        }
        if(url.startsWith("/user") && loginUser == null){
            response.sendRedirect("/login");
            return false;
        }
        if(url.startsWith("/datasource") && loginUser == null){
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
    //请求已经被处理，但在视图渲染前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
    //请求结果结果已经渲染好了，response没有进行返回但也无法修改reponse了（一般用于清理数据）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
