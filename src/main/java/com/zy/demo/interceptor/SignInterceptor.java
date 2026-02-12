package com.zy.demo.interceptor;

import com.zy.demo.config.WebMvcProperties;
import com.zy.demo.constant.ResponseEnum;
import com.zy.demo.exception.BusinessException;
import com.zy.demo.exception.InterceptorAbortException;
import com.zy.demo.handler.CacheRequestWrapper;
import com.zy.demo.pojo.BaseResponse;
import com.zy.demo.util.PwdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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
        String appNo = request.getHeader("appNo");
        String timestamp = request.getHeader("timestamp");
        String signData = request.getHeader("signData");

        CacheRequestWrapper cacheRequestWrapper = (CacheRequestWrapper) request;
        String bodyStr = cacheRequestWrapper.getBodyAsString();
        String requestMethod = request.getMethod().toUpperCase();
        String requestContextType = request.getContentType();
        boolean result = false;
        try {
            Assert.hasText(appNo, "appNo must not be empty");
            Assert.hasText(signData, "signData must not be empty");
            if (HttpMethod.POST.toString().equals(requestMethod) && MediaType.APPLICATION_JSON_VALUE.equals(requestContextType)) {
                String secret = this.webMvcProperties.getSign().getSecret();
                String currentSignData = PwdUtil.sha256Digest("appNo=" + appNo + "&timestamp=" + timestamp + "&bodyStr=" + bodyStr + secret);
                //验签
                if (signData.equals(currentSignData)) {
                    result = true;
                } else {
                    throw new BusinessException("验签不通过");
                }
            }
        } catch (Exception e) {
            BaseResponse<Object> baseResponse = BaseResponse.build(ResponseEnum.SIGN_FAIL.getCode(), ResponseEnum.SIGN_FAIL.getMsg() + e.getMessage(), null);
            throw new InterceptorAbortException(HttpServletResponse.SC_FORBIDDEN, baseResponse, e);
        }
        return result;
    }
}
