package com.zy.demo.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 国际化工具类
 *
 * @author zy
 */
public class I18nUtil {

    private final MessageSource messageSource;

    public I18nUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 获取国际化配置
     *
     * @param key key
     * @return value
     */
    public String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return this.messageSource.getMessage(key, null, locale);
    }

    /**
     * 获取国际化配置
     *
     * @param key     key
     * @param objects 动态参数
     * @return value
     */
    public String getMessage(String key, Object[] objects) {
        Locale locale = LocaleContextHolder.getLocale();
        return this.messageSource.getMessage(key, objects, locale);
    }
}
