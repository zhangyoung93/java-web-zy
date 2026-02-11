package com.zy.demo.filter;

import com.zy.demo.handler.CacheRequestWrapper;
import com.zy.demo.handler.CacheResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web过滤器
 *
 * @author zy
 */
public class BaseWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            //包装request
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            CacheRequestWrapper cacheRequestWrapper = new CacheRequestWrapper(request);
            //包装response
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            CacheResponseWrapper cacheResponseWrapper = new CacheResponseWrapper(response);
            //添加过滤
            filterChain.doFilter(cacheRequestWrapper, cacheResponseWrapper);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
