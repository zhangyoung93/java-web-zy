package com.zy.demo.util;

import java.util.Random;

/**
 * 雪花ID生成器
 *
 * @author zy
 */
public class SnowflakeIdGenerator {

    /**
     * 符号位数。符号位固定是0，不使用。
     */
    private static final long SYMBOL_BITS = 1L;

    /**
     * 时间戳位数。从指定EPOCH TIME开始的毫秒时间戳位数。
     */
    private static final long TIMESTAMP_BITS = 41L;

    /**
     * 节点ID位数。分布式系统中的唯一ID（静态配置、动态注册、机器地址）
     */
    private static final long NODE_ID_BITS = 10L;

    /**
     * 序列号位。同一节点同一毫秒内生成的序列号。
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 最大节点ID。10位节点ID最大值：1023
     */
    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;

    /**
     * 最大序列号。12位序列号最大值：4095
     */
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    /**
     * 时间戳左移（10+12）位，为节点ID与序列号预留空间
     */
    private static final long TIMESTAMP_OFFSET = SEQUENCE_BITS + NODE_ID_BITS;

    /**
     * 节点ID左移12位，为序列号预留空间
     */
    private static final long NODE_ID_OFFSET = SEQUENCE_BITS;

    /**
     * Twitter默认基准时间（2010-11-04 01:42:54 UTC），可自定义。
     */
    private static final long EPOCH = 1288834974657L;


    /**
     * 上一次生成ID的时间戳。保证可见性。
     */
    private volatile long lastTimestamp = -1L;

    /**
     * 当前节点ID
     */
    private final long nodeId;

    /**
     * 当前序列号。保证可见性。
     */
    private volatile long sequence = 0L;

    /**
     * 私有化构造方法，禁止外部new对象
     */
    private SnowflakeIdGenerator() {
        this.nodeId = getNodeId();
    }

    /**
     * 静态内部类实现创建单例对象
     */
    private static class SnowflakeIdGeneratorInstance {
        private static final SnowflakeIdGenerator INSTANCE = new SnowflakeIdGenerator();
    }

    /**
     * 获取单例对象
     *
     * @return SnowflakeIdGenerator
     */
    public static SnowflakeIdGenerator getInstance() {
        return SnowflakeIdGeneratorInstance.INSTANCE;
    }

    /**
     * 线程安全的ID生成方法（使用synchronized保证并发安全）
     *
     * @return 雪花算法唯一ID
     */
    public synchronized long nextId() {
        //获取当前时间戳
        long currentTimestamp = System.currentTimeMillis();

        //处理时间回拨问题（当前时间戳 < 上一次生成ID的时间戳）
        if (currentTimestamp < this.lastTimestamp) {
            throw new RuntimeException("时间回拨异常，无法生成雪花ID！上一次时间：" + this.lastTimestamp + "，当前时间：" + currentTimestamp);
        }

        //校验节点ID
        if (this.nodeId < 0 || this.nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException("节点ID超出有效范围：0 ~ " + MAX_NODE_ID);
        }

        //生成序列号
        if (currentTimestamp == this.lastTimestamp) {
            //同一毫秒内，序列号递增
            this.sequence = (this.sequence + 1) & MAX_SEQUENCE;
            //序列号达最大值，等待下一毫秒重置
            if (this.sequence == 0) {
                currentTimestamp = waitNextMillisecond(this.lastTimestamp);
            }
        } else {
            //不同毫秒，序列号重置为0
            this.sequence = 0L;
        }

        //更新上一次生成ID的时间戳
        this.lastTimestamp = currentTimestamp;

        //位运算拼接ID：时间戳 << 22 | 节点ID << 12 | 序列号
        return ((currentTimestamp - EPOCH) << TIMESTAMP_OFFSET) | (this.nodeId << NODE_ID_OFFSET) | this.sequence;
    }

    /**
     * 阻塞等待，直到获取下一毫秒的时间戳
     *
     * @param lastTimestamp 上一次生成ID的时间戳
     * @return 下一毫秒的时间戳
     */
    private long waitNextMillisecond(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 获取节点ID（具体实现方式静态配置、动态注册、本机信息）
     *
     * @return nodeId
     */
    private long getNodeId() {
        Random random = new Random();
        return random.nextInt((int) MAX_NODE_ID + 1);
    }
}