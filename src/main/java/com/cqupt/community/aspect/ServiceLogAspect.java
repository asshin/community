package com.cqupt.community.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Aspect
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.cqupt.community.service.*.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        // 用户[1.2.3.4],在[xxx],访问了[com.nowcoder.community.service.xxx()].
        /*
        * RequestContextHolder.getRequestAttributes()：这是一个静态方法，用于获取与当前线程关联的当前请求属性。它返回一个RequestAttributes实例，它是用于访问请求属性的通用接口。
(ServletRequestAttributes)：这是一个类型转换操作。它将getRequestAttributes()返回的RequestAttributes对象转换为ServletRequestAttributes。ServletRequestAttributes是Spring提供的RequestAttributes接口的特定实现，用于访问与Servlet相关的属性。
attributes.getRequest()：这个方法调用从ServletRequestAttributes实例中获取底层的HttpServletRequest对象。getRequest()方法返回与当前线程关联的当前请求对象。
通过执行这些代码行，您将可以访问HttpServletRequest对象，从而可以检索关于传入请求的信息，例如头信息、参数和请求URL等。*/
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes==null){
           return;
        }
        HttpServletRequest request = attributes.getRequest();//通过消费者调用service时没有request（不是通过controller掉的）
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));
    }

}
