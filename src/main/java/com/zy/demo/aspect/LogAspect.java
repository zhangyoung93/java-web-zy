package com.zy.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志切面
 *
 * @author zy
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 定义切点：控制器的所有方法或者注解标记的方法
     */
    @Pointcut("execution(* com.zy.demo.controller.*.*(..)) || @annotation(com.zy.demo.annotation.LogAspectPointcut)")
    public void pointcut() {

    }

    /**
     * 目标方法执行前、@Around通知后触发
     */
    @Before("pointcut()")
    public void before() {
    }

    /**
     * 目标方法正常返回结果后触发，可操作返回结果。
     *
     * @param result 目标方法的返回结果
     */
    @AfterReturning(value = "pointcut()", returning = "result")
    public void afterReturning(Object result) {
    }

    /**
     * 目标方法发生异常时触发
     *
     * @param e exception
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowing(Throwable e) {
    }

    /**
     * 目标方法执行后统一触发，无论执行正常或异常。
     */
    @After("pointcut()")
    public void after() {
    }

    /**
     * 环绕目标方法执行
     *
     * @param joinPoint 被拦截的目标方法
     * @return 目标方法返回值
     * @throws Throwable 异常
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object object = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        log.info("target method=[{}],args=[{}],cost time=[{}ms]", methodSignature.getMethod(), Arrays.toString(args), endTime - startTime);
        return object;
    }
}
