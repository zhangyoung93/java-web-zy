package com.zy.demo.annotation;

import java.lang.annotation.*;

/**
 * 自定义切点标记
 *
 * @author zy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JdkPointcut {

    String value() default "";
}
