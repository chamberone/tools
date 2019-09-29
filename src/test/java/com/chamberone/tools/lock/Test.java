package com.chamberone.tools.lock;

public class Test {

    public static void main(String[] args) {
        String result = LockUtils.<String> tryLockRun(RedisKeyGroup.prefix1, "", () -> {
            // 这里是你的处理代码
            return "hello world!";
        }, () -> {
            return "有其他程序正在运行";
        });
        System.out.println(result);
    }

}
