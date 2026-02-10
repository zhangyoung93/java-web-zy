package com.zy.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Spring容器事件监听器
 *
 * @author zy
 */
@Slf4j
@Component
public class ApplicationContextEventListener {

    @EventListener(value = ApplicationEnvironmentPreparedEvent.class)
    public void onApplicationEnvironmentPreparedEvent() {
        log.info("ApplicationContextEventListener-应用环境准备完成");
    }

    @EventListener(value = ApplicationContextInitializedEvent.class)
    public void onApplicationContextInitializedEvent() {
        log.info("ApplicationContextEventListener-Spring容器初始化完成");
    }

    @EventListener(value = ApplicationPreparedEvent.class)
    public void onApplicationPreparedEvent() {
        log.info("ApplicationContextEventListener-应用准备完成");
    }

    @EventListener(value = ContextRefreshedEvent.class)
    public void onContextRefreshedEvent() {
        log.info("ApplicationContextEventListener-Spring容器刷新完成");
    }

    @EventListener(value = ApplicationStartedEvent.class)
    public void onApplicationStartedEvent() {
        log.info("ApplicationContextEventListener-应用启动完成");
    }

    @EventListener(value = ApplicationReadyEvent.class)
    public void onApplicationReadyEvent() {
        log.info("ApplicationContextEventListener-应用就绪");
    }

    @EventListener(value = ApplicationFailedEvent.class)
    public void onApplicationFailedEvent() {
        log.info("ApplicationContextEventListener-应用启动失败");
    }
}
