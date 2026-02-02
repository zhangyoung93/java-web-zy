package com.zy.demo.annotation;

import java.lang.annotation.*;

/**
 * Spring Bean注解
 *
 * @author zy
 */
@Target(value = ElementType.TYPE)   //目标类型
@Retention(value = RetentionPolicy.RUNTIME) //保留策略
@Documented //是否可生成API到文档中
public @interface SpringBean {

    /**
     * Bean名称
     *
     * @return String
     */
    String value() default "";

    /**
     * 是否懒加载，默认false
     *
     * @return boolean
     */
    boolean lazyInit() default false;

    /**
     * bean作用域
     *
     * @return String
     */
    String scope() default "";
}
