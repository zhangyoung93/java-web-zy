package com.zy.demo.interceptor;

import com.zy.demo.config.WebMvcProperties;
import com.zy.demo.constant.ResponseEnum;
import com.zy.demo.util.HttpServletUtil;
import com.zy.demo.util.Sha256SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口验签拦截器
 *
 * @author zy
 */
@Slf4j
@Component
public class SignInterceptor implements HandlerInterceptor {

    private final WebMvcProperties webMvcProperties;

    public SignInterceptor(WebMvcProperties webMvcProperties) {
        this.webMvcProperties = webMvcProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestMethod = request.getMethod().toUpperCase();
        String requestContextType = request.getContentType();
        String appNo = request.getHeader("appNo");
        String timestamp = request.getHeader("timestamp");
        String signData = request.getHeader("signData");
        String bodyStr = HttpServletUtil.getRequestBody(request);
        boolean result = false;
        try {
            if (HttpMethod.POST.toString().equals(requestMethod) && MediaType.APPLICATION_JSON_VALUE.equals(requestContextType)) {
                result = Sha256SignUtil.verifySignData(appNo, timestamp, bodyStr, this.webMvcProperties.getSign().getSecret(), signData);
            }
        } catch (Exception e) {
            log.error(ResponseEnum.SIGN_FAIL.getMsg(), e);
            HttpServletUtil.writeResponse(response, ResponseEnum.SIGN_FAIL.getCode(), ResponseEnum.SIGN_FAIL.getMsg(), null);
        }
        return result;
    }
}
