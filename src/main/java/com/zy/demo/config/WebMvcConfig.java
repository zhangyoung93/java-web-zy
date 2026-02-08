package com.zy.demo.config;

import com.zy.demo.interceptor.IdempotentInterceptor;
import com.zy.demo.interceptor.SignInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        //注册接口验签拦截
        registry.addInterceptor(this.signInterceptor)
                .addPathPatterns(this.webMvcProperties.getSign().getIncludePaths());
        //注册接口幂等拦截
        Set<String> set = this.webMvcProperties.getIdempotent().getIncludePaths().keySet();
        List<String> list = new ArrayList<>(set);
        registry.addInterceptor(this.idempotentInterceptor)
                .addPathPatterns(list);
    }
}
