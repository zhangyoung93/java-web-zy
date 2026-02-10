package com.zy.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.zy.demo.constant.RedisConstant;
import com.zy.demo.executor.RedisPipelineThreadPool;
import com.zy.demo.pojo.RedisMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类
 *
 * @author zy
 */
@Slf4j
public class RedisOpUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedissonClient redissonClient;

    public RedisOpUtil(RedisTemplate<String, Object> redisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    public RedisConnectionFactory getConnectionFactory() {
        return this.redisTemplate.getConnectionFactory();
    }

    public boolean set(String key, Object value) {
        Assert.hasText(key, "key must not be empty!");
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return false;
    }

    public boolean set(String key, Object value, long timeout, TimeUnit timeUnit) {
        Assert.hasText(key, "key must not be empty!");
        try {
            this.redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            return true;
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return false;
    }

    /**
     * 原子性赋值
     *
     * @param key      key
     * @param value    value
     * @param timeout  存活期
     * @param timeUnit 存活期单位
     * @return boolean
     */
    public boolean setIfAbsent(String key, Object value, long timeout, TimeUnit timeUnit) {
        Assert.hasText(key, "key must not be empty!");
        Boolean result = false;
        try {
            result = this.redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return Boolean.TRUE.equals(result);
    }

    public Object getValue(String key) {
        Assert.hasText(key, "key must not be empty!");
        Object object = null;
        try {
            object = this.redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis-getValue操作异常", e);
        }
        return object;
    }

    public boolean hasKey(String key) {
        Assert.hasText(key, "key must not be empty!");
        boolean hasKey = false;
        try {
            hasKey = this.redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis-hasKey操作异常", e);
        }
        return hasKey;
    }

    public Long incr(String key) {
        Assert.hasText(key, "key must not be empty!");
        Long newValue = null;
        try {
            newValue = this.redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.error("Redis-incr操作异常", e);
        }
        return newValue;
    }

    public void hSetAll(String key, Map<String, Object> map) {
        Assert.hasText(key, "key must not be empty!");
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            log.error("Redis-hSetAll操作异常", e);
        }
    }

    /**
     * redis管道异步写入
     *
     * @param map map
     */
    public List<Object> asyncPipelineSet(Map<String, Object> map) throws Exception {
        Assert.notEmpty(map, "map must not be null!");
        //结果集
        List<Object> resultList = new ArrayList<>();
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            //批次结果集
            List<Object> list;
            List<String> keyList = Lists.newArrayList(map.keySet());
            //每100条数据用管道执行一次（建议值）
            List<List<String>> lists = Lists.partition(keyList, 100);
            for (List<String> subList : lists) {
                list = this.redisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public Object execute(RedisOperations redisOperations) throws DataAccessException {
                        RedisOperations<String, Object> ro = (RedisOperations<String, Object>) redisOperations;
                        for (String key : subList) {
                            if (key == null) {
                                log.error("asyncPipelineSet fail,key=null");
                                continue;
                            }
                            ro.opsForValue().set(key, map.get(key));
                        }
                        return null;
                    }
                });
                resultList.add(list);
            }
            return resultList;
        }, RedisPipelineThreadPool.getInstance());
        //为了方便本地测试，主线程阻塞等待获取线程池执行结果。
        completableFuture.get();
        return resultList;
    }

    /**
     * 生产消息（一对一）
     */
    public void sendMessage(RedisMessageDto redisMessageDto) {
        Assert.notNull(redisMessageDto, "redisMessageDto must not be null!");
        Date date = new Date();
        redisMessageDto.setOpTime(date);
        ObjectMapper objectMapper = new ObjectMapper();
        //如果直接传参实体类会导致无法序列化，改为map
        Map<String, Object> dataMap = (Map<String, Object>) objectMapper.convertValue(redisMessageDto, Map.class);
        Record<String, Map<String, Object>> record = StreamRecords.newRecord().in(RedisConstant.STREAM_KEY).ofMap(dataMap);
        this.redisTemplate.opsForStream().add(record);
    }

    /**
     * 发布消息（一对多）
     *
     * @param channelName     channelName
     * @param redisMessageDto redisMessageDto
     */
    public void publishMessage(String channelName, RedisMessageDto redisMessageDto) {
        Assert.hasText(channelName, "channelName must not be empty!");
        Assert.notNull(redisMessageDto, "redisMessageDto must not be empty!");
        this.redisTemplate.convertAndSend(channelName, redisMessageDto);
        log.info("publishMessage,channelName={},message={}", channelName, redisMessageDto);
    }

    /**
     * 创建Stream组
     *
     * @param key    stream key
     * @param group  stream group
     * @param offset 数据消费偏移量
     */
    public void createStreamGroup(String key, String group, String offset) {
        Assert.hasText(key, "key must not be empty!");
        Assert.hasText(group, "group must not be empty!");
        Assert.hasText(offset, "offset must not be empty!");
        this.redisTemplate.opsForStream().createGroup(key, ReadOffset.from(offset), group);
        log.info("createStreamGroup,key={},group={},offset{}", key, group, offset);
    }

    /**
     * 消息确认
     *
     * @param group   组
     * @param message 消息类
     */
    public void streamAck(String group, ObjectRecord<String, String> message) {
        Assert.hasText(group, "group must not be empty!");
        Assert.notNull(message, "message must not be null!");
        this.redisTemplate.opsForStream().acknowledge(group, message);
        log.info("streamAck,group={},message{}", group, message);
    }

    /**
     * redis事务
     *
     * @return List<Object>
     */
    public List<Object> transactionLock(String lockKey) {
        Assert.hasText(lockKey, "lockKey must not be empty!");
        //初始化lockKey，用于测试
        this.redisTemplate.opsForValue().set(lockKey, "0");
        List<Object> resultList = this.redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
                RedisOperations<String, Object> ro = (RedisOperations<String, Object>) redisOperations;
                //乐观锁。监听lock键
                ro.watch(lockKey);
                //开启事务
                ro.multi();
                //另一个线程修改lockKey的值
                //当前线程命令入队，
                ro.opsForValue().set(lockKey, "-1");
//                //取消事务
//                ro.discard();
                //提交事务，由于另一个线程修改，导致set无效。lockKey的值并不是-1。
                return ro.exec();
            }
        });
        log.info("transactionSet,resultList={}", resultList);
        return resultList;
    }

    /**
     * 分布式环境获取锁(阻塞，自动续期）
     *
     * @param lockKey 锁key
     */
    public void lock(String lockKey) {
        Assert.hasText(lockKey, "lockKey must not be empty!");
        RLock rLock = this.redissonClient.getLock(lockKey);
        rLock.lock();
    }

    /**
     * 分布式环境获取锁(阻塞，不能自动续期）
     *
     * @param lockKey  锁key
     * @param timeout  锁的超时时间
     * @param timeUnit 时间单位
     */
    public void lock(String lockKey, long timeout, TimeUnit timeUnit) {
        Assert.hasText(lockKey, "lockKey must not be empty!");
        RLock rLock = this.redissonClient.getLock(lockKey);
        rLock.lock(timeout, timeUnit);
    }

    /**
     * 分布式环境尝试获取锁（非阻塞）
     *
     * @param lockKey  锁key
     * @param waitTime 获取锁的等待时间
     * @param timeout  锁的超时时间。timeout=-1时开启自动续期
     * @param timeUnit 时间单位
     * @return 是否获取到锁
     * @throws InterruptedException InterruptedException
     */
    public boolean tryLock(String lockKey, long waitTime, long timeout, TimeUnit timeUnit) throws InterruptedException {
        Assert.hasText(lockKey, "lockKey must not be empty!");
        boolean hasLock;
        RLock rLock = this.redissonClient.getLock(lockKey);
        if (timeout < 0) {
            //如果锁的超时时间小于0，则是自动续期的锁
            hasLock = rLock.tryLock(waitTime, timeUnit);
        } else {
            //有超时时间的锁
            hasLock = rLock.tryLock(waitTime, timeout, timeUnit);
        }
        //模拟测试自动续期
//        Thread.sleep(60000L);
        return hasLock;
    }

    /**
     * 分布式环境释放锁
     *
     * @param lockKey 锁key
     */
    public void unlock(String lockKey) {
        Assert.hasText(lockKey, "lockKey must not be empty!");
        RLock rLock = this.redissonClient.getLock(lockKey);
        //只有拿到锁的线程才能释放锁
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlock();
        }
    }
}
