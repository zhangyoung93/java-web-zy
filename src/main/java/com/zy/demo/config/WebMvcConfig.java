package com.zy.demo.config;

import com.zy.demo.interceptor.IdempotentInterceptor;
import com.zy.demo.interceptor.SignInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Web MVC配置
 *
 * @author zy
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final WebMvcProperties webMvcProperties;

    private final SignInterceptor signInterceptor;

    private final IdempotentInterceptor idempotentInterceptor;

    public WebMvcConfig(WebMvcProperties webMvcProperties, SignInterceptor signInterceptor, IdempotentInterceptor idempotentInterceptor) {
        this.webMvcProperties = webMvcProperties;
        this.signInterceptor = signInterceptor;
        this.idempotentInterceptor = idempotentInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册验签拦截
        registry.addInterceptor(this.signInterceptor)
                .addPathPatterns(this.webMvcProperties.getSign().getIncludePaths());
        //注册幂等拦截
        List<String> list = this.webMvcProperties.getIdempotent().getIdempotentRuleList().stream().map(WebMvcProperties.IdempotentRule::getPath).distinct().collect(Collectors.toList());
        registry.addInterceptor(this.idempotentInterceptor).addPathPatterns(list);
    }
}
