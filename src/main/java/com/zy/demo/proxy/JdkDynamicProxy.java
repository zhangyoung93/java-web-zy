package com.zy.demo.proxy;

import com.zy.demo.annotation.SpringPointcut;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理
 *
 * @author zy
 */
@Slf4j
public class JdkDynamicProxy implements InvocationHandler {

    /**
     * 目标对象
     */
    private final Object target;

    public JdkDynamicProxy(Object target) {
        this.target = target;
    }

    /**
     * 创建代理对象
     *
     * @param target 目标对象
     * @return 代理对象
     */
    public static Object createProxy(Object target) {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();
        InvocationHandler invocationHandler = new JdkDynamicProxy(target);
        Object object = Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        log.info("JdkDynamicProxy-createProxy={}", object);
        return object;
    }

    /**
     * 自定义代理方法
     *
     * @param proxy  代理对象
     * @param method 目标方法
     * @param args   方法入参
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object object;
        if (!method.isAnnotationPresent(SpringPointcut.class)) {
            object = method.invoke(this.target, args);
        } else {
            long startTime = System.currentTimeMillis();
            object = method.invoke(this.target, args);
            long endTime = System.currentTimeMillis();
            log.info("target method=[{}],args=[{}],cost time=[{}ms]", method.getName(), Arrays.toString(args), endTime - startTime);
        }
        return object;
    }
}
