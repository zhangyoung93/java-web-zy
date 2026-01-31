package com.zy.demo.handler;

import org.springframework.beans.factory.DisposableBean;

/**
 * Bean销毁前执行
 *
 * @author zy
 */
public class LogDisposableBean implements DisposableBean {

    /**
     * Bean初销毁前执行的逻辑，比如释放文件句柄，同@PreDestroy
     *
     * @throws Exception Exception
     */
    @Override
    public void destroy() throws Exception {

    }
}
