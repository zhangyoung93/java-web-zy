package com.zy.demo.Consumer;

import com.zy.demo.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * redis消息消费者
 *
 * @author zy
 */
@Slf4j
@Component
public class RedisMessageConsumer {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisMessageConsumer(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        //创建消费者组
        StreamInfo.XInfoGroups xInfoGroups = this.redisTemplate.opsForStream().groups(RedisConstant.STREAM_KEY);
        if (xInfoGroups.isEmpty()) {
            this.redisTemplate.opsForStream().createGroup(RedisConstant.STREAM_KEY, ReadOffset.from(RedisConstant.OFFSET_ALL), RedisConstant.CONSUMER_GROUP);
        }
        //创建监听器配置类
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().batchSize(RedisConstant.BATCH_SIZE).pollTimeout(Duration.ofMillis(RedisConstant.POLL_TIMEOUT)).targetType(String.class).build();

        //创建监听器
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer.create(Objects.requireNonNull(this.redisTemplate.getConnectionFactory()), options);

        //监听器接收消息
        container.receive(
                Consumer.from(RedisConstant.CONSUMER_GROUP, RedisConstant.CONSUMER_NAME),
                StreamOffset.create(RedisConstant.STREAM_KEY, ReadOffset.lastConsumed()),
                message -> {
                    log.info("receive redis stream message,msgId={},msgValue={}", message.getId(), message.getValue());
                    try {
                        //消费消息
                        handleMessage(message);
                        //确认消息已消费
                        this.redisTemplate.opsForStream().acknowledge(RedisConstant.CONSUMER_GROUP, message);
                    } catch (Exception e) {
                        log.error("consume msg fail,save the fail message", e);
                        //记录死信数据
                        Map<String, String> map = new HashMap<>(8);
                        map.put(RedisConstant.STREAM_FAIL_ID, String.valueOf(message.getId()));
                        map.put(RedisConstant.STREAM_FAIL_VALUE, message.getValue());
                        map.put(RedisConstant.STREAM_FAIL_TIME, String.valueOf(System.currentTimeMillis()));
                        map.put(RedisConstant.STREAM_FAIL_STATUS, "-1");
                        String redisKey = RedisConstant.STREAM_FAIL_KEY + message.getId();
                        this.redisTemplate.opsForHash().putAll(redisKey, map);
                    }
                }
        );

        //启动监听器
        container.start();
        log.info("redis消息消费者已启动，监听队列={}，消费者组={}，消费者={}", RedisConstant.STREAM_KEY, RedisConstant.CONSUMER_GROUP, RedisConstant.CONSUMER_NAME);
    }

    /**
     * 处理消息
     *
     * @param message message
     */
    private void handleMessage(ObjectRecord<String, String> message) {
        log.info("handleMessage");
    }
}
