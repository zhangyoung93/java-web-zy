package com.zy.demo.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * redis管道线程工厂
 *
 * @author zy
 */
public class RedisPipelineThreadFactory implements ThreadFactory {

    /**
     * 线程池ID
     */
    private static final AtomicInteger POOL_ID = new AtomicInteger(1);

    /**
     * 线程组
     */
    private final ThreadGroup threadGroup;

    /**
     * 线程ID
     */
    private final AtomicInteger threadId = new AtomicInteger(1);

    /**
     * 线程名称前缀
     */
    private final String namePrefix;

    /**
     * 是否守护线程
     */
    private final boolean daemon;

    /**
     * 线程优先级
     */
    private final int priority;

    public RedisPipelineThreadFactory() {
        //禁止线程守护。设置默认优先级，降低线程饥饿的概率。
        this(false, Thread.NORM_PRIORITY);
    }

    public RedisPipelineThreadFactory(boolean daemon, int priority) {
        if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException("priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }
        SecurityManager securityManager = System.getSecurityManager();
        this.threadGroup = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" + POOL_ID.getAndIncrement() + "-thread-";
        this.daemon = daemon;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        //stackSize是线程的栈内存大小，stackSize=0表示使用默认值
        Thread thread = new Thread(this.threadGroup, runnable, this.namePrefix + this.threadId.getAndIncrement(), 0);
        thread.setDaemon(this.daemon);
        thread.setPriority(this.priority);
        return thread;
    }
}
