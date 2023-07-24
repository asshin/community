package com.cqupt.community.controller.interceptor;

import com.cqupt.community.entity.LoginTicket;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CookieUtil;
import com.cqupt.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author zsw
 * @create 2023-06-27 15:51
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    private static final Logger logger = LoggerFactory.getLogger(AlphaInterceptor.class);

    //在controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("prehandle:"+handler.toString());
        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket!=null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                //根据模板查user
                User user = userService.getUserById(loginTicket.getUserId());
                //在本次请求中持有用户
                hostHolder.setUser(user);
                //构建用户认证的结果，并存入securityContext ，以便于security进行授权
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                      user,user.getPassword(),userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));

            }
        }

        return true;

    }
    //在controller后执行templateEngine之前执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }

    }
    //templateEngine之后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
         hostHolder.clear();
         SecurityContextHolder.clearContext();
    }

}
