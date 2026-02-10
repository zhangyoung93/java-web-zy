package com.zy.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * Bean工厂实例化处理器
 *
 * @author zy
 */
@Slf4j
@Component
public class LogBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    /**
     * BeanFactory实例化后，Bean实例化前执行的方法
     *
     * @param configurableListableBeanFactory configurableListableBeanFactory
     * @throws BeansException BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        log.info("BeanFactory已经实例化，即将加载的Bean如下：");
        Iterator<String> iterator = configurableListableBeanFactory.getBeanNamesIterator();
    }
}
