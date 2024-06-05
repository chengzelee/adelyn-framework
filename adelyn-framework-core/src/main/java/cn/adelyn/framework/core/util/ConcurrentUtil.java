package cn.adelyn.framework.core.util;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentUtil {

    private static final ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newVirtualThreadPerTaskExecutor());

    public static <T> Future<T> processTask(Callable<T> task) {
        return executorService.submit(task);
    }

    public static void processTask(Runnable task) {
        executorService.submit(task);
    }
}
