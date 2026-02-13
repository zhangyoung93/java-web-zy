package com.zy.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.demo.handler.CacheRequestWrapper;
import com.zy.demo.handler.CacheResponseWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web过滤器
 *
 * @author zy
 */
@Component
public class BaseWebFilter implements Filter {

    private final ObjectMapper objectMapper;

    public BaseWebFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            //包装request
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            CacheRequestWrapper cacheRequestWrapper = new CacheRequestWrapper(request, this.objectMapper);
            //包装response
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            CacheResponseWrapper cacheResponseWrapper = new CacheResponseWrapper(response);
            //添加过滤
            filterChain.doFilter(cacheRequestWrapper, cacheResponseWrapper);
            cacheResponseWrapper.writeToResponse();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
