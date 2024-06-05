package cn.adelyn.framework.core.util;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.*;

public class ConcurrentLimitUtil {
    private final Semaphore semaphore;
    private final ExecutorService executorService;

    public ConcurrentLimitUtil(int maxConcurrency) {
        semaphore = new Semaphore(maxConcurrency);
        executorService = TtlExecutors.getTtlExecutorService(Executors.newVirtualThreadPerTaskExecutor());
    }

    public void processTask(Runnable task) {
        try {
            // 提交任务前获取锁，降低锁竞争时间
            semaphore.acquire(); // 获取信号量
            executorService.execute(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release(); // 释放信号量
        }
    }

    public <T> Future<T> processTask(Callable<T> task) {
        try {
            // 提交任务前获取锁，降低锁竞争时间
            semaphore.acquire(); // 获取信号量
            return executorService.submit(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release(); // 释放信号量
        }
    }

    /**
     * <a href="https://juejin.cn/post/7220613829586911288">api详解</a>
     * <a href="https://cloud.tencent.com/developer/article/1330497">为什么要shutDown?</a>
     * 线程池shutDown之后，想继续提交任务只能再new 一个线程池
     * 1. 不 shutDown, isTerminated 始终为 false，无法实现我们工具期望执行完所有任务后主线程继续执行的目标
     * 2. 不 shutDown, JVM 退出的条件是当前不存在用户线程，而线程池默认的 ThreadFactory 创建的线程是用户线程
     *      而线程池里面的 核心线程 是一直会存在的，keepAliveTime 是核心线程之外线程的失效时间，如果没有任务则会阻塞，所以线程池里面的用户线程一直会存在.
     *      而shutdown方法的作用就是让这些核心线程终止，也就是让 jvm 能退出
     */
    public void awaitAllTasksTerminate(long checkIntervalMillis) {
        executorService.shutdown();

        try {
            while (!executorService.isTerminated()) {
                Thread.sleep(checkIntervalMillis);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
