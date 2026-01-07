package com.zy.demo.config;

import com.zy.demo.interceptor.SqlTimeConsumeMonitorPlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置
 *
 * @author zy
 */
@Configuration
public class MybatisConfig {

    private final MybatisPluginConfiguration mybatisPluginConfiguration;

    public MybatisConfig(MybatisPluginConfiguration mybatisPluginConfiguration) {
        this.mybatisPluginConfiguration = mybatisPluginConfiguration;
    }

    /**
     * 注册自定义插件
     *
     * @return SqlTimeConsumeMonitorPlugin
     */
    @Bean
    public SqlTimeConsumeMonitorPlugin sqlTimeConsumeMonitorPlugin() {
        SqlTimeConsumeMonitorPlugin sqlTimeConsumeMonitorPlugin = new SqlTimeConsumeMonitorPlugin();
        sqlTimeConsumeMonitorPlugin.setMybatisPluginConfiguration(this.mybatisPluginConfiguration);
        return sqlTimeConsumeMonitorPlugin;
    }

    /**
     * 利用ConfigurationCustomizer在mybatis配置对象初始化完成前，做自定义增强。不影响mybatis自动配置。
     *
     * @param sqlTimeConsumeMonitorPlugin sqlTimeConsumeMonitorPlugin
     * @return ConfigurationCustomizer
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer(Interceptor sqlTimeConsumeMonitorPlugin) {
        return configuration -> {
            //添加sql耗时监控插件
            configuration.addInterceptor(sqlTimeConsumeMonitorPlugin);
        };
    }
}
