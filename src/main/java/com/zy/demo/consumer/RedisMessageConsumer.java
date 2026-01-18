package com.zy.demo.consumer;

import com.zy.demo.constant.RedisConstant;
import com.zy.demo.util.RedisOpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
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

    private final RedisOpUtil redisOpUtil;

    public RedisMessageConsumer(RedisOpUtil redisOpUtil) {
        this.redisOpUtil = redisOpUtil;
    }

    @PostConstruct
    public void init() {
        //创建消费者组
        if (!this.redisOpUtil.hasKey(RedisConstant.STREAM_KEY)) {
            this.redisOpUtil.createStreamGroup(RedisConstant.STREAM_KEY, RedisConstant.CONSUMER_GROUP, RedisConstant.OFFSET_ALL);
        }
        //创建监听器配置类
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder().batchSize(RedisConstant.BATCH_SIZE).pollTimeout(Duration.ofMillis(RedisConstant.POLL_TIMEOUT)).targetType(String.class).build();

        //创建监听器
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer.create(Objects.requireNonNull(this.redisOpUtil.getConnectionFactory()), options);

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
                        this.redisOpUtil.streamAck(RedisConstant.CONSUMER_GROUP, message);
                    } catch (Exception e) {
                        log.error("consume msg fail,save the fail message", e);
                        //记录死信数据
                        Map<String, Object> map = new HashMap<>(8);
                        map.put(RedisConstant.STREAM_FAIL_ID, String.valueOf(message.getId()));
                        map.put(RedisConstant.STREAM_FAIL_VALUE, message.getValue());
                        map.put(RedisConstant.STREAM_FAIL_TIME, String.valueOf(System.currentTimeMillis()));
                        map.put(RedisConstant.STREAM_FAIL_STATUS, "-1");
                        String redisKey = RedisConstant.STREAM_FAIL_KEY + message.getId();
                        this.redisOpUtil.hSetAll(redisKey, map);
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
