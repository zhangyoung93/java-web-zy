package com.zy.demo.executor;

import com.zy.demo.factory.RedisPipelineThreadFactory;
import com.zy.demo.handler.RedisPipelineRejectedHandler;

import java.util.concurrent.*;

/**
 * redis管道线程池。为了直观显示线程池参数，修饰为static，实际情况放到局部方法中定义。
 *
 * @author zy
 */
public class RedisPipelineThreadPool {

    /**
     * 核心线程数，一般配置CPU+1
     */
    private static final int CORE_POOL_SIZE = 5;

    /**
     * 最大线程数=核心线程数（完全发挥CPU性能）
     */
    private static final int MAXIMUM_POOL_SIZE = 5;

    /**
     * 非核心线程存活时间(单位：秒)
     */
    private static final long KEEP_ALIVE_TIME = 30L;

    /**
     * 时间单位
     */
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    /**
     * 工作队列（线程满后任务先进入队列等待）
     */
    private static final BlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(100);

    /**
     * 线程创建工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new RedisPipelineThreadFactory();

    /**
     * 拒绝执行策略（线程达到最大值且工作队列占满的情况下，如何处理新任务）
     * jdk提供的4种拒绝策略：
     * 1、AbortPolicy(默认)：终止策略，抛出异常。适用金融等需要感知失败的场景。
     * 2、CallerRunsPolicy：调用者（提交任务的线程，如主线程）执行策略。跳过start()直接调用run()方法。
     * 3、DiscardOldestPolicy：丢弃最老任务策略。丢弃队头任务，重新尝试提交任务。适用优先执行新任务场景。
     * 4、DiscardPolicy：丢弃策略。静默丢弃被拒绝的任务。适用结果不重要场景，如日志采集。
     */
    private static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = new RedisPipelineRejectedHandler();

    /**
     * 设置单例
     */
    private ThreadPoolExecutor threadPoolExecutor;

    private RedisPipelineThreadPool() {

    }

    public static ThreadPoolExecutor getInstance() {
        ThreadPoolExecutor threadPoolExecutor = Singleton.INSTANCE;
        //允许核心线程按照过期时间销毁。节省资源。
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    private static class Singleton {
        private static final ThreadPoolExecutor INSTANCE = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TIME_UNIT, WORK_QUEUE, THREAD_FACTORY, REJECTED_EXECUTION_HANDLER);
    }
}
