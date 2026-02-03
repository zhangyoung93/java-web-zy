package com.zy.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyTest {

    public static void main(String[] args) {
        System.out.println("main test start...");
        long start = System.currentTimeMillis();
        mainTest();
        long end = System.currentTimeMillis();
        System.out.println("main test end，cost time=" + (end - start) + "ms");
    }

    private static void mainTest() {

    }

    @Test
    public void context() throws InterruptedException {
        System.out.println("context test start...");
        long start = System.currentTimeMillis();
        contextTest();
        long end = System.currentTimeMillis();
        System.out.println("contest test end，cost time=" + (end - start) + "ms");
        Thread.sleep(2000L);
    }

    private void contextTest() {

    }
}
