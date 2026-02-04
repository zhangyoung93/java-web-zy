package com.zy.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redisson配置
 *
 * @author zy
 */
@ConfigurationProperties(prefix = "redisson")
public class RedissonConfiguration {

    /**
     * 是否覆盖自动配置，手动注册Bean
     */
    private boolean enable = false;

    /**
     * redis模式（single/sentinel/cluster）
     */
    private String mode = "single";

    /**
     * single模式下的redis连接URL，防止spring.redis.url解析出现的bug问题
     */
    private String url = "redis://127.0.0.1:6379";

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
