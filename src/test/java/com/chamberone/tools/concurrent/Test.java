package com.chamberone.tools.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 简单多线程运行工具
 * 
 * @author guoping
 *
 */
public class Test {

    public static void main(String[] args) {
        List<String> test = new ArrayList<>();
        test.add("1");
        test.add("2");
        test.add("3");

        ParallelFunction<String, Integer> runner =
                new ParallelFunction<String, Integer>(test, (element) -> Integer.parseInt(element), 5);
        Map<String, Integer> result = runner.run();

    }

}
