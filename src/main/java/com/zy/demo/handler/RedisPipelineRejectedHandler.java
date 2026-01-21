package com.zy.demo.handler;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * redis管道线程池拒绝执行策略
 *
 * @author zy
 */
public class RedisPipelineRejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        throw new RejectedExecutionException("redis管道线程池拒绝执行=====>Task " + runnable.toString() + " rejected from " + executor.toString());
    }
}
