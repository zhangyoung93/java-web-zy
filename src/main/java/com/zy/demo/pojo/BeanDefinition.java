package com.zy.demo.pojo;

import java.util.Set;

/**
 * Spring Bean定义实体类
 *
 * @author zy
 */
public class BeanDefinition {

    /**
     * Bean名称
     */
    private String beanName;

    /**
     * Bean的类对象
     */
    private Class<?> beanClass;

    /**
     * 依赖对象。用于判断多个Bean的创建顺序。
     */
    private Set<String> dependsOn;

    public BeanDefinition(String beanName, Class<?> beanClass, Set<String> dependsOn) {
        this.beanName = beanName;
        this.beanClass = beanClass;
        this.dependsOn = dependsOn;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Set<String> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(Set<String> dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "beanName='" + beanName + '\'' +
                ", beanClass=" + beanClass +
                ", dependsOn=" + dependsOn +
                '}';
    }
}
