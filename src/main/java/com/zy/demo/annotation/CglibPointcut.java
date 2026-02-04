package com.zy.demo.annotation;

import java.lang.annotation.*;

/**
 * CGLIB动态代理切点
 *
 * @author zy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CglibPointcut {
}
