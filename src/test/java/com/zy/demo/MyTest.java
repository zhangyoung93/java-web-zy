package com.zy.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest {

    public static void main(String[] args) {
        System.out.println("单元测试开始");
        long start = System.currentTimeMillis();
        mainTest();
        long end = System.currentTimeMillis();
        System.out.println("单元测试结束，程序执行耗费时间=" + (end - start) + "ms");
    }

    private static void mainTest() {

    }

    @Test
    public void context() throws InterruptedException {
        System.out.println("集成测试开始");
        long start = System.currentTimeMillis();
        contextTest();
        long end = System.currentTimeMillis();
        System.out.println("集成测试结束，程序执行耗费时间=" + (end - start) + "ms");
        Thread.sleep(2000L);
    }

    private void contextTest() {

    }
}
