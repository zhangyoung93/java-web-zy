package com.zy.demo.annotation;

import java.lang.annotation.*;

/**
 * 依赖Bean
 *
 * @author zy
 */
@Target(ElementType.FIELD)  //注解在字段上
@Retention(RetentionPolicy.RUNTIME)   //仅运行时保留
@Documented
public @interface DependBean {

    boolean require() default true;
}
