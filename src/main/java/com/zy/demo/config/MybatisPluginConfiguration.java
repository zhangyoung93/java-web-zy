package com.zy.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Mybatis插件配置
 *
 * @author zy
 */
@ConfigurationProperties(prefix = "mybatis.plugin")
@Component
public class MybatisPluginConfiguration {

    /**
     * sql耗时阈值，单位毫秒
     */
    int sqlCostTimeThreshold = 2000;

    public int getSqlCostTimeThreshold() {
        return sqlCostTimeThreshold;
    }

    public void setSqlCostTimeThreshold(int sqlCostTimeThreshold) {
        this.sqlCostTimeThreshold = sqlCostTimeThreshold;
    }
}
