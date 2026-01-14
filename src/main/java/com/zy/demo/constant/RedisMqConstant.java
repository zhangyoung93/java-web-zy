package com.zy.demo.constant;

/**
 * REDIS MQ 常量
 * @author zy
 */
public class RedisMqConstant {

    /**
     * STREAM_KEY
     */
    public static final String STREAM_KEY = "biz";

    /**
     * 消费者
     */
    public static final String CONSUMER_NAME = "consumer-zy";

    /**
     * 消费者组
     */
    public static final String CONSUMER_GROUP = "consumer-group-zy";

    /**
     * 消费全量消息的偏移量，包含历史消息
     */
    public static final String OFFSET_ALL = "0-0";

    /**
     * 消费最新消息的偏移量，不包含历史消息
     */
    public static final String OFFSET_NEW = "$";

    /**
     * 批量拉取消息的数量
     */
    public static final int BATCH_SIZE = 10;

    /**
     * 批量拉取消息阻塞等待时间，单位毫秒
     */
    public static final long POLL_TIMEOUT = 5000;

    /**
     * 死信数据记录key
     */
    public static final String STREAM_FAIL_KEY = "stream:fail:";

    /**
     * 死信数据字段：消息ID、消息内容、失败时间、状态
     */
    public static final String STREAM_FAIL_ID = "msgId";
    public static final String STREAM_FAIL_VALUE = "value";
    public static final String STREAM_FAIL_TIME = "time";
    public static final String STREAM_FAIL_STATUS = "status";
}
