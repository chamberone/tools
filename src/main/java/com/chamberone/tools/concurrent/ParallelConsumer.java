package com.chamberone.tools.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * 多线程执行封装<br>
 * 无返回值版
 * 
 * @author guoping.zhang
 *
 */
public class ParallelConsumer<T> extends ParallelRunner<T> {

    private Consumer<T> execute;

    public ParallelConsumer(List<T> elements, Consumer<T> execute, int threadSize) {
        super(elements, threadSize);
        this.execute = execute;
    }

    public void run() {
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        T t = queue.poll();
                        while (null != t) {
                            try {
                                execute.accept(t);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            t = queue.poll();
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            }.start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("InterruptedException");
        }
    }
}
