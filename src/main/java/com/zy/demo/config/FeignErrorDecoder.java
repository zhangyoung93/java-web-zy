package com.zy.demo.config;

import com.zy.demo.exception.BusinessException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Feign异常处理
 *
 * @author zy
 */
@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String msg;
        try {
            log.error("\n调用feign接口异常:{} \n请求url:{} \n请求headers:{}", methodKey, response.request().url(), response.request().headers());
            Reader reader = response.body().asReader(StandardCharsets.UTF_8);
            String body = IOUtils.toString(reader);
            log.error("调用feign错误的响应信息:{}", body);
            msg = "接口处理失败";
        } catch (Exception e) {
            msg = e.getMessage();
            log.error("解析feign响应异常", e);
        }
        return new BusinessException(msg);
    }
}
