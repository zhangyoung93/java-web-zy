package com.zy.demo.bean;

import com.zy.demo.annotation.SpringBean;
import lombok.extern.slf4j.Slf4j;

/**
 * BeanB
 *
 * @author zy
 */
@Slf4j
@SpringBean(scope = "singleton")
public class BeanB {

    public void bTest() {
        log.info("bTest");
    }
}
