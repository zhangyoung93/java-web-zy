package com.zy.demo.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.demo.config.WebMvcProperties;
import com.zy.demo.constant.RedisConstant;
import com.zy.demo.constant.ResponseEnum;
import com.zy.demo.exception.BusinessException;
import com.zy.demo.exception.InterceptorAbortException;
import com.zy.demo.handler.CacheRequestWrapper;
import com.zy.demo.pojo.BaseResponse;
import com.zy.demo.util.RedisOpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 接口幂等拦截器
 *
 * @author zy
 */
@Slf4j
@Component
public class IdempotentInterceptor implements HandlerInterceptor {

    private final RedisOpUtil redisOpUtil;

    private final WebMvcProperties webMvcProperties;

    private final ObjectMapper objectMapper;

    public IdempotentInterceptor(RedisOpUtil redisOpUtil, WebMvcProperties webMvcProperties, ObjectMapper objectMapper) {
        this.redisOpUtil = redisOpUtil;
        this.webMvcProperties = webMvcProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //请求映射路径
        String servletPath = request.getServletPath();
        try {
            boolean result;
            //幂等校验字段
            String idempotentField = this.webMvcProperties.getIdempotent().getIdempotentRuleList().stream().filter(idempotentRule -> servletPath.equals(idempotentRule.getPath())).map(WebMvcProperties.IdempotentRule::getField).findFirst().orElse(null);
            Assert.hasText(idempotentField, "idempotentField not exists,servletPath=" + servletPath);
            //判断请求数据是对象还是数组
            CacheRequestWrapper cacheRequestWrapper = (CacheRequestWrapper) request;
            String bodyStr = cacheRequestWrapper.getBodyAsString();
            JsonNode jsonNode = this.objectMapper.readTree(bodyStr);
            if (jsonNode.isArray()) {
                List<Map<String, Object>> mapList = objectMapper.readValue(bodyStr, new TypeReference<List<Map<String, Object>>>() {
                });
                //JSON数组用幂等校验字段作为排序字段
                mapList.sort((o1, o2) -> {
                    Long l1 = (Long) o1.get(idempotentField);
                    Long l2 = (Long) o2.get(idempotentField);
                    return l1.compareTo(l2);
                });
                //对原请求数据做MD5计算，性能影响小
                String md5Str = DigestUtils.md5DigestAsHex(mapList.toString().getBytes(StandardCharsets.UTF_8));
                //判断JSON数组的幂等性
                result = this.redisOpUtil.setIfAbsent(RedisConstant.IDEMPOTENT_KEY + servletPath + md5Str, "1", 1, TimeUnit.HOURS);
            } else {
                //判断JSON对象的幂等性
                result = this.redisOpUtil.setIfAbsent(RedisConstant.IDEMPOTENT_KEY + servletPath + idempotentField, "1", 1, TimeUnit.HOURS);
            }
            if (!result) {
                throw new BusinessException("请求数据已处理");
            }
        } catch (Exception e) {
            int status = HttpServletResponse.SC_FORBIDDEN;
            BaseResponse<Object> baseResponse = BaseResponse.build(ResponseEnum.IDEMPOTENT_FAIL.getCode(), ResponseEnum.IDEMPOTENT_FAIL.getMsg() + e.getMessage(), null);
            if (e instanceof BusinessException) {
                //幂等校验，给客户端制造返回正常的结果
                status = HttpServletResponse.SC_OK;
                baseResponse = BaseResponse.success();
            }
            //抛出拦截器的特殊异常
            throw new InterceptorAbortException(status, baseResponse, e);
        }
        return true;
    }
}
