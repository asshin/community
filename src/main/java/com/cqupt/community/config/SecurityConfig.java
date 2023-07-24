package com.cqupt.community.config;

import com.cqupt.community.util.CommunityConstant;

import com.cqupt.community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;


import javax.naming.ConfigurationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zsw
 * @create 2023-07-24 14:26
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {

    @Override
    public  void configure(WebSecurity web) throws Exception{

        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //授权
         http.authorizeRequests().antMatchers(
                 "/user/setting",
                 "/discuss/add",
                 "/comment/add/**",
                 "/letter/**",
                 "/notice/**",
                 "/like",
                 "/follow",
                 "/unfollow"
         ).hasAnyAuthority(
                 AUTHORITY_USER,
                 AUTHORITY_USER,
                 AUTHORITY_MODERATOR
         ).anyRequest()  //任何的其他请求
                 .permitAll()//都直接允许
         .and().csrf().disable();
         //权限不够处理
        http.exceptionHandling().authenticationEntryPoint(
                new AuthenticationEntryPoint() {
                    //没有登录
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestedWith = request.getHeader("X-Requested-With");
                        //如果X-Requested-With==null则说明此请求为同步请求
                        // 如果X-Requested-With==XMLHttpRequest则说明为异步请求
                        if ("XMLHttpRequest".equals(xRequestedWith)){
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer=response.getWriter();
                            writer.write(CommunityUtil.getJsonString(403,"你还没有登录"));
                        }else {
                            response.sendRedirect(request.getContextPath()+"/login");
                        }
                    }
                }
        )
                .accessDeniedHandler(
                        //权限不足
                        new AccessDeniedHandler() {
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                                String xRequestedWith = request.getHeader("X-Requested-With");
                                if ("XMLHttpRequest".equals(xRequestedWith)){
                                    response.setContentType("application/plain;charset=utf-8");
                                    PrintWriter writer=response.getWriter();
                                    writer.write(CommunityUtil.getJsonString(403,"你没有权限访问此功能！"));
                                }else {
                                    response.sendRedirect(request.getHeader(request.getContextPath()+"/denied"));
                                }
                            }
                        }
                );
        //Security 底层默认拦截/logout请求,进行退出处理
        //覆盖它的默认逻辑,才能执行自己的代码
        http.logout().logoutUrl("/securitylogout");//将它退出拦截的请求转到一个不存在的路径
    }


}
