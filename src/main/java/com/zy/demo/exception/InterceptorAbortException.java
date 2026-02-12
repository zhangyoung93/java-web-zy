package com.zy.demo.exception;

import com.zy.demo.pojo.BaseResponse;

/**
 * 拦截器终止异常
 *
 * @author zy
 */
public class InterceptorAbortException extends RuntimeException {

    private final int status;

    private final BaseResponse<Object> baseResponse;

    public InterceptorAbortException(int status, BaseResponse<Object> baseResponse, Throwable throwable) {
        super(throwable);
        this.status = status;
        this.baseResponse = baseResponse;
    }

    public int getStatus() {
        return status;
    }

    public BaseResponse<Object> getBaseResponse() {
        return baseResponse;
    }
}
