package cn.adelyn.framework.core.trace;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MDCVirtualThreadExecutorService extends AbstractExecutorService {

    private final ExecutorService executorService;

    public MDCVirtualThreadExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public <T> Future<T> submit(@NonNull Callable<T> callable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return executorService.submit(() -> {
            if (context != null) {
                //将父线程的MDC内容传给子线程
                MDC.setContextMap(context);
            }
            try {
                //执行任务
                return callable.call();
            } finally {
                MDC.clear();
            }
        });
    }

    public void execute(@NonNull Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        executorService.execute(() -> {
            if (context != null) {
                //将父线程的MDC内容传给子线程
                MDC.setContextMap(context);
            }
            try {
                //执行任务
                runnable.run();
            } finally {
                MDC.clear();
            }
        });
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public void close() {
        executorService.close();
    }
}
