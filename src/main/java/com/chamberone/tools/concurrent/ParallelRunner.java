package com.chamberone.tools.concurrent;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 多线程执行封装基类
 * 
 * @author guoping.zhang
 *
 */
abstract public class ParallelRunner<T> {

    protected int threadSize;
    protected ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<T>(10000);

    protected ParallelRunner(List<T> elements, int threadSize) {
        this.threadSize = threadSize;
        int rest = queue.remainingCapacity() - elements.size();
        if (rest < 0) {
            throw new RuntimeException("exceed max size");
        }
        queue.addAll(elements);
    }

}
