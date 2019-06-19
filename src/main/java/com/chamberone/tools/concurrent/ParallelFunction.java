package com.chamberone.tools.concurrent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

/**
 * 多线程执行封装<br>
 * 返回值版
 * 
 * @author guoping.zhang
 *
 */
public class ParallelFunction<T, R> extends ParallelRunner<T> {

    private Function<T, R> function;

    public ParallelFunction(List<T> elements, Function<T, R> function, int threadSize) {
        super(elements, threadSize);
        this.function = function;
    }

    /**
     * 获取返回结果，如果有异常，可能会少于输入数目
     * 
     * @return
     */
    public Map<T, R> run() {
        //int total = queue.size();
        //AtomicInteger count = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        Map<T, R> resultMap = new ConcurrentHashMap<>();
        for (int i = 0; i < threadSize; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        T t = queue.poll();
                        while (null != t) {
                            R r = null;
                            try {
                                r = function.apply(t);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            } finally {
                                resultMap.put(t, r);
                            }
//                            int c = count.incrementAndGet();
//                            if (c % 1000 == 0) {
//                                System.out.println(c + "/" + total);
//                            }
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
        return resultMap;
    }
    
}
