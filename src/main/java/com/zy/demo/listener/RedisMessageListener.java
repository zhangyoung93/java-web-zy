package com.zy.demo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * redis消息监听器
 *
 * @author zy
 */
@Slf4j
@Component
public class RedisMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {
        //实际主题名称
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        //消息内容
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        //主题名称模板，例如pattern-topic的模板
        String bytesStr = new String(bytes, StandardCharsets.UTF_8);
        log.info("RedisMessageListener=====>onMessage,channel={},body{},bytes={}", channel, body, bytesStr);
    }
}
