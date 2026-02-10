package com.zy.demo.bean;

import com.zy.demo.annotation.DependBean;
import com.zy.demo.annotation.SpringBean;
import lombok.extern.slf4j.Slf4j;

/**
 * BeanA
 *
 * @author zy
 */
@Slf4j
@SpringBean(scope = "singleton", lazyInit = false)
public class BeanA {

    @DependBean
    private BeanB beanB;

    public void aTest() {
        System.out.println("aTest,beanB=" + this.beanB);
    }
}
