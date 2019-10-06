package cn.miya.reactor.utils;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.*;

public class ThreadPoolFactory {

    public static ExecutorService getThreadPool(int size, String name) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix(name + "- ").build();
        return new ThreadPoolExecutor(size, size,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }
}