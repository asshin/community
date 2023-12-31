package com.cqupt.community.controller.advice;

import com.cqupt.community.util.CommunityUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zsw
 * @create 2023-07-06 17:02
 */
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
     private  static  final Logger logger =   LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public  void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
         logger.error("服务器发生异常:"+e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
                 logger.error(element.toString());
        }
        /*如果是异步请求则返回json，否则返回网页*/
        String xRequestedWith=request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer =response.getWriter();
            writer.write(CommunityUtil.getJsonString(1,"服务器异常"));
        }else {
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }
}
