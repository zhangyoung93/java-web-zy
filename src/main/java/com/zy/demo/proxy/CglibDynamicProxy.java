package com.zy.demo.proxy;

import com.zy.demo.annotation.CglibPointcut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * CGLIB动态代理
 *
 * @author zy
 */
@Slf4j
public class CglibDynamicProxy {

    private CglibDynamicProxy() {

    }

    /**
     * 创建代理对象
     *
     * @param target 目标类
     * @return 代理子类
     */
    public static Object createProxy(Object target) {
        //创建增强器
        Enhancer enhancer = new Enhancer();
        //设置继承的父类，即目标类
        enhancer.setSuperclass(target.getClass());
        //定义拦截方法
        enhancer.setCallback(new MethodInterceptor() {
            /**
             * 拦截方法
             * @param o 目标对象
             * @param method 目标方法
             * @param objects 方法入参
             * @param methodProxy 目标类的子代理类
             * @return 方法返回值
             * @throws Throwable 异常
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Object object;
                //方法所属的类被@CglibPointcut注解才会拦截
                if (method.getDeclaringClass().isAnnotationPresent(CglibPointcut.class)) {
                    long startTime = System.currentTimeMillis();
                    //使用子类代理对象调用父类的目标方法，不需要反射，性能更好
                    object = methodProxy.invokeSuper(o, objects);
                    long endTime = System.currentTimeMillis();
                    log.info("CGLIB target method=[{}],args=[{}],cost time=[{}ms]", method.getName(), Arrays.toString(objects), endTime - startTime);
                } else {
                    object = methodProxy.invokeSuper(o, objects);
                }
                return object;
            }
        });
        //增强器创建对象
        return enhancer.create();
    }
}
