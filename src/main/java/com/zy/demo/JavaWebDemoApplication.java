package com.zy.demo;

import com.spring4all.swagger.EnableSwagger2Doc;
import com.zy.demo.bean.SpringBootBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * springboot启动类
 *
 * <p>
 * 注解@SpringBootApplication包含3个子注解：
 * 1、@SpringBootConfiguration
 * 声明启动类是SpringBoot配置类，可以注册Bean。
 * 2、@EnableAutoConfiguration
 * 开启Bean自动配置。加载自动配置的Bean。
 * 从META-INF/spring.factories加载org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * 或者从META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports文件加载。
 *
 * 3、@ComponentScan
 * 设置Bean扫描路径：扫描当前注解所在类的包及其所有子包
 * <p>
 *
 * 注解@MapperScan生成的mybatis代理对象底层依赖SqlSessionTemplate，线程安全
 *
 * @author zy
 */
@SpringBootApplication
@EnableSwagger2Doc
@MapperScan(basePackages = "com.zy.demo.dao.mysql")
public class JavaWebDemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(JavaWebDemoApplication.class, args);
        System.out.println("容器类型：" + context.getClass().getName());
    }

    @Bean(initMethod = "init")
    public SpringBootBean springBootBean() {
        return new SpringBootBean();
    }
}
