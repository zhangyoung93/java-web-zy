package com.zy.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.zy.demo.util.RedisOpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 *
 * @author zy
 */
@Configuration
public class RedisConfig {

    /**
     * 配置redisTemplate
     * redis底层存储是字节数组，如果不设置序列化，直接把Java对象传给redisTemplate，会触发类型转换异常。
     *
     * @param redisConnectionFactory redisConnectionFactory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置redis key的序列化类StringRedisSerializer，高效易读
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //设置redis value的序列化类Jackson2JsonRedisSerializer，性能更好
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        //增强ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        /*
            设置序列化对象可见性。
            PropertyAccessor.ALL表示序列化类中所有的属性访问方法。
            JsonAutoDetect.Visibility.ANY表示序列化任何访问权限的属性，包含private。
         */
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        /*
            处理反序列化class信息丢失问题。
            LaissezFaireSubTypeValidator.instance表示不限制反序列化的class类型。
            ObjectMapper.DefaultTyping.NON_FINAL表示非final修饰的类能写入class信息。
         */
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        //把自定义objectMapper绑定到JSON序列化类
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        //最后设置value的序列化器
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        //开启事务支持。设置为关闭。redis是弱一致性事务，如果与spring事务绑定会影响性能。
        redisTemplate.setEnableTransactionSupport(false);
        //初始化配置
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * redis操作工具类交给IOC管理，并注入上边自定义的redisTemplate
     *
     * @param redisTemplate redisTemplate
     * @return RedisOpUtil
     */
    @Bean
    public RedisOpUtil redisOpUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisOpUtil(redisTemplate);
    }
}
