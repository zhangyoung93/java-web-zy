package com.zy.demo.factory;

import com.zy.demo.pojo.BeanDefinition;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory抽象类
 *
 * @author zy
 */
public abstract class BeanFactory {

    /**
     * BeanDefinition
     */
    private final Map<String, BeanDefinition> beanDefinitionMap;

    /**
     * Bean单例池。一级缓存
     */
    private final Map<String, Object> singletonObjects;

    /**
     * 二级缓存
     */
    private final Map<String, Object> earlySingletonObjects;

    /**
     * 三级缓存
     */
    private final Map<String, BeanFactory> singletonFactories;

    protected BeanFactory() {
        this.beanDefinitionMap = new ConcurrentHashMap<>(256);
        this.singletonObjects = new ConcurrentHashMap<>(256);
        this.earlySingletonObjects = new ConcurrentHashMap<>(16);
        this.singletonFactories = new HashMap<>(16);
    }

    protected Map<String, BeanDefinition> getBeanDefinitionMap() {
        return this.beanDefinitionMap;
    }

    protected void addBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        Assert.hasText(beanName, "beanName must not be empty");
        Assert.notNull(beanDefinition, "beanDefinition must not be empty");
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    protected BeanDefinition getBeanDefinition(String beanName) {
        Assert.hasText(beanName, "beanName must not be empty");
        return this.beanDefinitionMap.get(beanName);
    }

    protected void addSingletonObject(String beanName, Object object) {
        this.singletonObjects.put(beanName, object);
    }

    protected Object getSingletonObject(String beanName) {
        Assert.hasText(beanName, "beanName must not be empty");
        return this.singletonObjects.get(beanName);
    }
}
