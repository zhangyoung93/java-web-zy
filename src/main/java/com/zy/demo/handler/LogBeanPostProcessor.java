package com.zy.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Bean初始化处理器
 *
 * @author zy
 */
@Slf4j
@Component
public class LogBeanPostProcessor implements BeanPostProcessor {

    /**
     * Bean初始化之前执行
     *
     * @param bean     bean
     * @param beanName beanName
     * @return Bean
     * @throws BeansException BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Bean初始化之后执行
     *
     * @param bean     bean
     * @param beanName beanName
     * @return Bean
     * @throws BeansException BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
