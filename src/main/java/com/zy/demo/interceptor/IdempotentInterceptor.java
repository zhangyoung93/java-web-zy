package com.zy.demo.interceptor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.demo.config.WebMvcProperties;
import com.zy.demo.constant.RedisConstant;
import com.zy.demo.constant.ResponseEnum;
import com.zy.demo.util.HttpServletUtil;
import com.zy.demo.util.RedisOpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public IdempotentInterceptor(RedisOpUtil redisOpUtil, WebMvcProperties webMvcProperties) {
        this.redisOpUtil = redisOpUtil;
        this.webMvcProperties = webMvcProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = false;
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            String bodyStr = HttpServletUtil.getRequestBody(request);
            //请求映射路径
            String servletPath = request.getServletPath();
            //幂等判断字段
            String idempotentField = webMvcProperties.getIdempotent().getIncludePaths().get(servletPath);
            ObjectMapper objectMapper = new ObjectMapper();
            //判断请求数据是对象还是数组
            JsonNode jsonNode = objectMapper.readTree(bodyStr);
            if (jsonNode.isArray()) {
                List<Map<String, Object>> mapList = objectMapper.readValue(bodyStr, new TypeReference<List<Map<String, Object>>>() {
                });
                //数组需要排序后再参与幂等校验
                mapList.sort((o1, o2) -> {
                    Long l1 = (Long) o1.get(idempotentField);
                    Long l2 = (Long) o2.get(idempotentField);
                    return l1.compareTo(l2);
                });
                //对原请求数据做MD5计算，性能影响小
                String md5Str = DigestUtils.md5DigestAsHex(mapList.toString().getBytes(StandardCharsets.UTF_8));
                //redis设置数组的幂等条件
                result = this.redisOpUtil.setIfAbsent(RedisConstant.IDEMPOTENT_KEY + servletPath + md5Str, "1", 1, TimeUnit.HOURS);
            } else {
                //redis设置对象的幂等条件
                result = this.redisOpUtil.setIfAbsent(RedisConstant.IDEMPOTENT_KEY + servletPath + idempotentField, "1", 1, TimeUnit.HOURS);
            }
        }
        if (!result) {
            log.error(ResponseEnum.IDEMPOTENT_FAIL.getMsg());
            HttpServletUtil.writeResponse(response, ResponseEnum.IDEMPOTENT_FAIL.getCode(), ResponseEnum.IDEMPOTENT_FAIL.getMsg(), null);
        }
        return result;
    }
}
