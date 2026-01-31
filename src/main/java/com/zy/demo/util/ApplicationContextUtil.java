package com.zy.demo.util;

import com.zy.demo.event.MailSendEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * 应用上下文
 *
 * @author zy
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取环境信息
     *
     * @return Environment
     */
    public Environment getEnvironment() {
        return this.applicationContext.getEnvironment();
    }

    /**
     * 获取配置项的值
     *
     * @param name         配置项name
     * @param defaultValue 配置项默认值
     * @return 配置项value
     */
    public String getProperty(String name, String defaultValue) {
        return this.applicationContext.getEnvironment().getProperty(name, defaultValue);
    }

    /**
     * 发送邮件
     *
     * @param content 邮件内容
     */
    public void sendMail(String content) {
        this.applicationContext.publishEvent(new MailSendEvent(this.applicationContext, content));
    }
}
