package com.zy.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.zy.demo.constant.RedisConstant;
import com.zy.demo.executor.RedisPipelineThreadPool;
import com.zy.demo.pojo.RedisMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

    public RedisOpUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean set(String key, Object value) {
        try {
            Assert.notNull(key, "key must not be null!");
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return false;
    }

    public boolean set(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            Assert.notNull(key, "key must not be null!");
            this.redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            return true;
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return false;
    }

    public Object getValue(String key) {
        Object object = null;
        try {
            Assert.notNull(key, "key must not be null!");
            object = this.redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return object;
    }

    public boolean hasKey(String key) {
        try {
            Assert.notNull(key, "key must not be null!");
            this.redisTemplate.hasKey(key);
            return true;
        } catch (Exception e) {
            log.error("Redis-exists操作异常", e);
        }
        return false;
    }

    public Long incr(String key) {
        Long newValue = null;
        try {
            Assert.notNull(key, "key must not be null!");
            newValue = this.redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.error("Redis-exists操作异常", e);
        }
        return newValue;
    }

    public void hSetAll(String key, Map<String, Object> map) {
        try {
            Assert.notNull(key, "key must not be null!");
            this.redisTemplate.opsForHash().putAll(key, map);
        } catch (Exception e) {
            log.error("Redis-exists操作异常", e);
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
     * 生产消息
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
     * 发布消息
     *
     * @param channelName     channelName
     * @param redisMessageDto redisMessageDto
     */
    public void publishMessage(String channelName, RedisMessageDto redisMessageDto) {
        this.redisTemplate.convertAndSend(channelName, redisMessageDto);
        log.info("publishMessage=====>channelName={},message={}", channelName, redisMessageDto);
    }

    /**
     * redis事务
     *
     * @return List<Object>
     */
    public List<Object> transactionLock(String lockKey) {
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
}
