package com.zy.demo.config;

import com.zy.demo.util.ApplicationContextUtil;
import com.zy.demo.util.I18nUtil;
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
     *
     * @return ApplicationContextUtil
     */
    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }
}
