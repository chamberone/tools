package com.chamberone.tools.lock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.SingleServerConfig;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁相关操作,基于Redisson
 * 
 * @author guoping
 *
 */
public class LockUtils {

    public static final String SEPERATOR = ":";

    /**
     * redis配置信息，根据实际情况配置
     */
    private static final String host = null;
    private static final String port = null;
    private static final String password = null;

    private static RedissonClient redisson;

    /**
     * 带锁运行,默认5分钟
     * 
     * @param group 锁分组
     * @param lockName 锁名称
     * @param success 获取锁成功执行
     * @param failed 获取锁失败执行
     * @return
     */
    public static <T> T tryLockRun(RedisKeyGroup group, String lockName, Supplier<T> success, Supplier<T> failed) {
        return tryLockRun(group, lockName, success, failed, 5, TimeUnit.MINUTES);
    }

    /**
     * 带锁运行,默认5分钟
     * 
     * @param group 锁分组
     * @param lockName 锁名称
     * @param failed 获取锁失败执行
     * @param success 获取锁成功执行
     * @return
     */
    public static <T> T tryLockRun(RedisKeyGroup group, String lockName, Supplier<T> success, Supplier<T> failed,
            long timeToLive, TimeUnit timeUnit) {
        if (null == success) {
            return null;
        }
        RLock lock = getLock(group, lockName);
        boolean ret = lock.tryLock();
        if (ret) {
            try {
                // 超时保护
                if (null == timeUnit) {
                    lock.expire(5, TimeUnit.MINUTES);
                } else {
                    lock.expire(timeToLive, timeUnit);
                }
                return success.get();
            } finally {
                lock.unlock();
            }
        } else {
            // 获取锁失败
            if (null != failed) {
                return failed.get();
            } else {
                return null;
            }
        }
    }
    

    /**
     * 串行运行
     * 
     * @param group
     * @param lockName
     * @param supplier
     * @return
     */
    public static <T> T sequentialRun(RedisKeyGroup group, String lockName, Supplier<T> supplier) {
        RLock lock = getLock(group, lockName);
        try {
            // 获取锁
            lock.lock(-1, TimeUnit.SECONDS);
            return supplier.get();
        } finally {
            // 释放锁
            try {
                lock.unlock();// 可能会失败
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    private static RLock getLock(RedisKeyGroup redisKeyGroup, String name) {
        String lockKey = redisKeyGroup + SEPERATOR + name;
        if (null == redisson || redisson.isShutdown()) {
            synchronized (LockUtils.class) {
                if (null == redisson || redisson.isShutdown()) {
                    redisson = getRedissonClient();
                }
            }
        }
        return redisson.getLock(lockKey);
    }

    private static RedissonClient getRedissonClient() {
        org.redisson.config.Config config = new org.redisson.config.Config();
        SingleServerConfig singleConfig = config.useSingleServer();
        singleConfig.setAddress("redis://" + host + ":" + port);
        singleConfig.setPassword(password);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

}
