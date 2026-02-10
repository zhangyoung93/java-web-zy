package com.zy.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 自定义ApplicationContextInitializer
 *
 * @author zy
 */
@Slf4j
public class LogApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * Spring容器创建并注入环境对象之后，容器刷新之前执行
     *
     * @param configurableApplicationContext context
     */
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        log.info("LogApplicationContextInitializer-initialize()");
    }
}
