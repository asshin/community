package com.cqupt.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.cqupt.community.service.*.*(..))")
    public void pointcut() {

    }
    @Pointcut("execution(* com.cqupt.community.service.LikeService.like(..))")
    public void likecut() {

    }
    @Before("likecut()")
    public void before() {


    }
    //


    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        System.out.println("around after");
        return obj;
    }

}
