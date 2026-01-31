package com.zy.demo.util;

import com.zy.demo.event.MailSendEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
     * 发送邮件
     *
     * @param content 邮件内容
     */
    public void sendMail(String content) {
        this.applicationContext.publishEvent(new MailSendEvent(this.applicationContext, content));
    }
}
