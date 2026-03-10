package com.zy.demo.config;

import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;
import com.zy.demo.util.ApplicationContextUtil;
import com.zy.demo.util.I18nUtil;
import com.zy.demo.util.JasyptUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具类注册到IOC
 *
 * @author zy
 */
@Configuration
public class UtilsConfig {

    /**
     * 国际化
     *
     * @param messageSource MessageSource
     * @return I18nUtil
     */
    @Bean
    public I18nUtil i18nUtil(MessageSource messageSource) {
        return new I18nUtil(messageSource);
    }

    /**
     * 应用上下文
     * 指定Bean初始化方法
     *
     * @return ApplicationContextUtil
     */
    @Bean(initMethod = "init")
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }

    /**
     * Jasypt属性
     *
     * @return JasyptEncryptorConfigurationProperties
     */
    @Bean
    @ConfigurationProperties(prefix = "jasypt.encryptor")
    public JasyptEncryptorConfigurationProperties jasyptEncryptorConfigurationProperties() {
        return new JasyptEncryptorConfigurationProperties();
    }

    /**
     * Jasypt加密工具类
     *
     * @param jasyptEncryptorConfigurationProperties Jasypt属性
     * @return JasyptUtil
     */
    @Bean
    public JasyptUtil jasyptUtil(JasyptEncryptorConfigurationProperties jasyptEncryptorConfigurationProperties) {
        return new JasyptUtil(jasyptEncryptorConfigurationProperties);
    }
}
