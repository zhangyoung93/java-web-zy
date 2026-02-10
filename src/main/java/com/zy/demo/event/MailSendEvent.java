package com.zy.demo.event;

import org.springframework.context.ApplicationEvent;

/**
 * 邮件发送事件
 *
 * @author zy
 */
public class MailSendEvent extends ApplicationEvent {

    /**
     * 邮件内容
     */
    private final String content;

    public MailSendEvent(Object source, String content) {
        super(source);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
