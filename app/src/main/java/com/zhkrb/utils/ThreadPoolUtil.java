package com.zhkrb.utils;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;


/**
 * @description 线程池
 * @author zhkrb
 * @time 2020/6/23
 */

public class ThreadPoolUtil {
    private static ExecutorService executorService;

    private static ExecutorService getExecutorService() {
        if (executorService == null) {
            int coreSize = 2;
            long keepAliveTime = 20L;
            return new ThreadPoolExecutor(coreSize, Integer.MAX_VALUE,
                    keepAliveTime, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(64),
                    new ThreadPoolExecutor.DiscardOldestPolicy());
        }
        return executorService;
    }

    public static void submit(Runnable runnable) {
        getExecutorService().execute(runnable);
    }

    public static void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CustomThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "异步线程池-" + POOL_NUMBER.getAndIncrement() + "-线程-";
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    private static class ExceptionCaughtThreadPool extends ThreadPoolExecutor {


        public ExceptionCaughtThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if (t!=null){
                L.e(t.toString());
            }
        }
    }

}
