package com.zy.demo.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 自定义ApplicationRunner
 *
 * @author zy
 */
@Slf4j
@Component
public class LogApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("LogApplicationRunner,args={}", args);
        //获取非选项参数（不带--前缀的参数）
        List<String> list = args.getNonOptionArgs();
        //获取选项参数（带--前缀的参数）
        Set<String> set = args.getOptionNames();
        //读取参数值
        if (args.containsOption("port")) {
            List<String> port = args.getOptionValues("port");
        }
        //获取原始参数数组，同commandLine的args[]
        String[] sourceArgs = args.getSourceArgs();
    }
}
