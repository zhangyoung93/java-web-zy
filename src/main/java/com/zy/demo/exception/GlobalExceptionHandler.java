package com.zy.demo.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 全局异常处理
 *
 * @author zy
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 处理拦截器终止异常
     *
     * @param e        InterceptorAbortException
     * @param response response
     * @throws IOException IOException
     */
    @ExceptionHandler(InterceptorAbortException.class)
    public void handleInterceptorAbort(InterceptorAbortException e, HttpServletResponse response) throws IOException {
        response.setStatus(e.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(this.objectMapper.writeValueAsString(e.getBaseResponse()));
    }
}
