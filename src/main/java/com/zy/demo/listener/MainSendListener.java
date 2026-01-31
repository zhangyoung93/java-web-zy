package com.zy.demo.listener;

import com.zy.demo.event.MailSendEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 邮件发送监听器
 *
 * @author zy
 */
@Slf4j
@Component
public class MainSendListener implements ApplicationListener<MailSendEvent> {

    @Override
    public void onApplicationEvent(MailSendEvent mailSendEvent) {
        log.info("receive main content={}", mailSendEvent.getContent());
    }
}
