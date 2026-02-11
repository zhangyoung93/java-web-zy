package com.zy.demo.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Feign配置类
 * 该类添加到FeignClient中会自动配置，不需要@Configuration
 *
 * @author zy
 */
@Slf4j
public class FeignConfig implements RequestInterceptor {

    /**
     * 注册异常处理类
     *
     * @return FeignErrorDecoder
     */
    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    /**
     * 拦截HTTP请求
     *
     * @param requestTemplate requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return;
        }
        //获取原请求头信息
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            //将原请求头信息复制到feign请求中
            requestTemplate.header(name, value);
        }
    }
}
