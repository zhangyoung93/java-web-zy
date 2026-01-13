package com.zy.demo.util;

import com.google.common.collect.Lists;
import com.zy.demo.exception.BusinessException;
import com.zy.demo.executor.RedisPipelineThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
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
     * @param mapList mapList
     */
    public void asyncPipelineSet(List<Map<String, String>> mapList) {
        CompletableFuture.supplyAsync(() -> {
            List<List<Map<String, String>>> lists = Lists.partition(mapList, 1000);
            List<Object> resultList = null;
            for (List<Map<String, String>> maps : lists) {
                //每1000条数据用pipeline执行一次
                resultList = this.redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
                    //打开管道
                    redisConnection.openPipeline();
                    for (Map<String, String> map : maps) {
                        map.forEach((key, value) -> {
                            redisConnection.set(key.getBytes(), value.getBytes());
                        });
                    }
                    //executePipelined方法会自动收尾，不要二次处理，直接返回null就行
                    return null;
                });
            }
            return resultList;
        }, RedisPipelineThreadPool.getInstance());
    }
}
