package com.zy.demo.util;

import com.google.common.collect.Lists;
import com.zy.demo.exception.BusinessException;
import com.zy.demo.executor.RedisPipelineThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

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

    private void checkKey(String key) throws BusinessException {
        if (key == null) {
            throw new BusinessException("Redis操作异常！key不能为空");
        }
    }

    public boolean set(String key, Object value) {
        try {
            checkKey(key);
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return false;
    }

    public boolean set(String key, Object value, long timeout, TimeUnit timeUnit) {
        try {
            checkKey(key);
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
            checkKey(key);
            object = this.redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis-set操作异常", e);
        }
        return object;
    }

    public boolean hasKey(String key) {
        try {
            checkKey(key);
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
            checkKey(key);
            newValue = this.redisTemplate.opsForValue().increment(key);
        } catch (Exception e) {
            log.error("Redis-exists操作异常", e);
        }
        return newValue;
    }

    public void hSetAll(String key, Map<String, Object> map) {
        try {
            checkKey(key);
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
        if (MapUtils.isEmpty(map)) {
            throw new Exception("asyncPipelineSet fail,map=null");
        }
        //结果集
        List<Object> resultList = new ArrayList<>();
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            //批次结果集
            List<Object> list;
            List<String> keyList = Lists.newArrayList(map.keySet());
            //每1000条数据用管道执行一次
            List<List<String>> lists = Lists.partition(keyList, 1000);
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
}
