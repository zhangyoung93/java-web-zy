package com.zy.demo;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * springboot启动类
 * MapperScan注解生成的mybatis代理对象底层依赖SqlSessionTemplate，线程安全
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

}
