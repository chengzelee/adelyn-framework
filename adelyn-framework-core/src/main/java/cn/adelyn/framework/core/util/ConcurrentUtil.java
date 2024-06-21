package cn.adelyn.framework.core.util;

import cn.adelyn.framework.core.trace.MDCVirtualThreadExecutorService;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentUtil {

    private static final ExecutorService executorService = new MDCVirtualThreadExecutorService(Executors.newVirtualThreadPerTaskExecutor());

    public static <T> Future<T> processTask(Callable<T> task) {
        return executorService.submit(task);
    }

    public static void processTask(Runnable task) {
        executorService.execute(task);
    }
}
