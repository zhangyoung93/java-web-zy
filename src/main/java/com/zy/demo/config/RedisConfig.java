package com.zy.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.demo.constant.RedisConstant;
import com.zy.demo.listener.RedisMessageListener;
import com.zy.demo.util.RedisOpUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

import java.util.List;

/**
 * redis配置类
 *
 * @author zy
 */
@Configuration
public class RedisConfig {

    @Value("${redisson.mode:single}")
    private String redisMode;

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

    /**
     * redis消息监听器bean
     *
     * @param redisConnectionFactory redisConnectionFactory
     * @param redisMessageListener   redisMessageListener
     * @return RedisMessageListenerContainer
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, RedisMessageListener redisMessageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        //注册channel主题
        redisMessageListenerContainer.addMessageListener(redisMessageListener, new ChannelTopic(RedisConstant.CHANNEL_TOPIC));
        //注册pattern主题
        redisMessageListenerContainer.addMessageListener(redisMessageListener, new PatternTopic(RedisConstant.PATTERN_TOPIC));
        return redisMessageListenerContainer;
    }

    /**
     * RedissonClient
     *
     * @param redisProperties redisProperties
     * @return RedissonClient
     * @throws Exception Exception
     */
    @ConditionalOnProperty(prefix = "redisson", name = "enable", havingValue = "true")
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) throws Exception {
        Config config = new Config();
        List<String> nodeList;
        String[] nodes;
        switch (this.redisMode) {
            case "single":
                config.useSingleServer().setAddress(redisProperties.getUrl())
                        .setPassword(redisProperties.getPassword())
                        .setDatabase(redisProperties.getDatabase());
                break;
            case "sentinel":
                nodeList = redisProperties.getSentinel().getNodes();
                Assert.notEmpty(nodeList, "redis sentinel mode,nodes must not be null!");
                nodes = nodeList.stream().map(node -> "redis://" + node).toArray(String[]::new);
                config.useSentinelServers().addSentinelAddress(nodes)
                        .setMasterName(redisProperties.getSentinel().getMaster())
                        .setPassword(redisProperties.getPassword())
                        .setDatabase(redisProperties.getDatabase());
                break;
            case "cluster":
                nodeList = redisProperties.getCluster().getNodes();
                Assert.notEmpty(nodeList, "redis cluster mode,nodes must not be null!");
                nodes = nodeList.stream().map(node -> "redis://" + node).toArray(String[]::new);
                config.useClusterServers().addNodeAddress(nodes)
                        .setPassword(redisProperties.getPassword());
                break;
            default:
                throw new Exception("redisMode错误");
        }
        return Redisson.create(config);
    }
}
