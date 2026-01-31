package com.zy.demo.handler;

import org.springframework.beans.factory.InitializingBean;

/**
 * Bean初始化后执行的方法，在postProcessAfterInitialization()之前
 *
 * @author zy
 */
public class LogInitializingBean implements InitializingBean {

    /**
     * Bean初始化后执行的逻辑，比如线程池初始化，同@PostConstruct
     *
     * @throws Exception Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
