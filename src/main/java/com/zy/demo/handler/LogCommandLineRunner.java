package com.zy.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 自定义CommandLineRunner
 *
 * @author zy
 */
@Slf4j
@Component
public class LogCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("LogCommandLineRunner,args={}", Arrays.toString(args));
    }
}
