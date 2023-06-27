package com.cqupt.community.controller.interceptor;

import com.cqupt.community.LoginRequired;
import com.cqupt.community.entity.LoginTicket;
import com.cqupt.community.entity.User;
import com.cqupt.community.util.CookieUtil;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author zsw
 * @create 2023-06-27 20:56
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
    //在controller之前执行
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if (handler instanceof HandlerMethod){
           HandlerMethod handlerMethod=(HandlerMethod)handler;
           Method method = handlerMethod.getMethod();
           LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
           if (loginRequired!=null&&hostHolder.getUser()==null){
               response.sendRedirect(request.getContextPath()+"/login");
               return false;

           }
       }



       return  true;

    }
}
