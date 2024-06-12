package cn.adelyn.framework.core.trace;

import org.slf4j.MDC;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public class MdcVirtualThreadTaskExecutor extends TaskExecutorAdapter {

    public MdcVirtualThreadTaskExecutor(Executor concurrentExecutor) {
        super(concurrentExecutor);
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return super.submit(() -> {
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

    @Override
    public void execute(Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        super.execute(() -> {
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
}
