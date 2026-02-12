package com.zy.demo.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * sentinel请求来源解析
 *
 * @author zy
 */
@Component
public class SentinelRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        //优先从请求头获取来源应用
        String origin = httpServletRequest.getHeader("service-name");
        if (StringUtils.isBlank(origin)) {
            //默认来源
            origin = "default";
        }
        return origin;
    }
}
