package com.zy.demo.factory;

import com.zy.demo.annotation.DependBean;
import com.zy.demo.annotation.SpringBean;
import com.zy.demo.pojo.BeanDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SpringBootBeanFactory
 *
 * @author zy
 */
@Slf4j
public class SpringBootBeanFactory extends BeanFactory {

    private SpringBootBeanFactory() {
        //初始化BeanFactory
        super();
        //扫描并注册BeanDefine
        scanAndRegistryBeanDefine("com.zy.demo.bean");
        //创建对象并注册到单例池
        registrySingletonBeans();
    }

    private static class SpringBeanFactoryHolder {
        private static final SpringBootBeanFactory INSTANCE = new SpringBootBeanFactory();
    }

    /**
     * 获取单例
     *
     * @return BeanFactory
     */
    public static SpringBootBeanFactory getInstance() {
        return SpringBeanFactoryHolder.INSTANCE;
    }

    /**
     * 扫描并注册BeanDefine
     *
     * @param location 扫描路径
     */
    private void scanAndRegistryBeanDefine(String location) {
        URL url = this.getClass().getClassLoader().getResource(location.replace(".", "/"));
        Assert.notNull(url, "bean location not exists");
        File baseFile = new File(url.getFile());
        Assert.notNull(url, "baseFile not exists");
        File[] baseFiles = baseFile.listFiles();
        Assert.notNull(baseFiles, "baseFiles not exists");
        for (File file : baseFiles) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                scanAndRegistryBeanDefine(location + "." + fileName);
            } else {
                if (fileName.endsWith(".class")) {
                    String className = location + "." + fileName.substring(0, fileName.lastIndexOf("."));
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (clazz.isAnnotationPresent(SpringBean.class)) {
                            SpringBean springBean = clazz.getAnnotation(SpringBean.class);
                            String scope = springBean.scope();
                            boolean lazyInit = springBean.lazyInit();
                            if (!"singleton".equals(scope) || lazyInit) {
                                log.warn("bean is not singleton or bean is lazyInit,continue——————springBean={}", springBean);
                                continue;
                            }
                            String beanName = springBean.value();
                            if (StringUtils.isBlank(beanName)) {
                                String simpleName = clazz.getSimpleName();
                                beanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
                            }
                            Field[] fields = clazz.getDeclaredFields();
                            Set<String> dependBeanNameSet = new HashSet<>();
                            if (fields.length > 0) {
                                for (Field field : fields) {
                                    if (field.isAnnotationPresent(DependBean.class)) {
                                        String simpleName = field.getType().getSimpleName();
                                        String dependBeanName = Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
                                        dependBeanNameSet.add(dependBeanName);
                                    }
                                }
                            }
                            BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz, dependBeanNameSet);
                            super.addBeanDefinition(beanName, beanDefinition);
                            log.info("添加Bean定义信息,beanDefinition={}", beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error("扫描Bean异常", e);
                    }
                }
            }
        }
    }

    /**
     * 注册单例池
     */
    private void registrySingletonBeans() {
        Map<String, BeanDefinition> beanDefinitionMap = super.getBeanDefinitionMap();
        if (MapUtils.isEmpty(beanDefinitionMap)) {
            log.error("registrySingletonBeans-beanDefinitionMap must not be empty");
            return;
        }
        beanDefinitionMap.forEach((beanName, map) -> {
            try {
                Object object = map.getBeanClass().getDeclaredConstructor().newInstance();
                super.addSingletonObject(beanName, object);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("创建单例对象失败", e);
            }
        });
    }

    /**
     * 获取单例
     *
     * @param beanName beanName
     * @return Object
     */
    public Object getBean(String beanName) {
        Assert.hasText(beanName, "beanName must not be empty");
        BeanDefinition beanDefinition = super.getBeanDefinition(beanName);
        Assert.notNull(beanDefinition, "beanDefinition is null");
        Object beanInstance = super.getSingletonObject(beanName);
        Set<String> set = beanDefinition.getDependsOn();
        if (CollectionUtils.isNotEmpty(set)) {
            for (String dependOn : set) {
                Object dependOnInstance = super.getSingletonObject(dependOn);
                try {
                    Class<?> clazz = beanInstance.getClass();
                    Field field = clazz.getDeclaredField(dependOn);
                    field.setAccessible(true);
                    field.set(beanInstance, dependOnInstance);
                } catch (Exception e) {
                    log.error("获取单例失败", e);
                }
            }
        }
        return beanInstance;
    }
}
