package com.zy.demo.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zy.demo.constant.ResponseEnum;

/**
 * 基础应答实体（泛型版）
 *
 * @param <T> 响应数据的具体类型
 * @author zy
 */
public class BaseResponse<T> {
    // 编码
    private String code;
    // 描述
    private String msg;
    // 数据
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    // 返回值改为泛型T，无需强制转换
    public T getData() {
        return data;
    }

    // 参数改为泛型T，编译期检查类型
    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static <T> BaseResponse<T> build(String code, String msg, T data) {
        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setCode(code);
        baseResponse.setMsg(msg);
        if (data != null) {
            baseResponse.setData(data);
        }
        return baseResponse;
    }

    public static <T> BaseResponse<T> success(T data) {
        return build(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMsg(), data);
    }

    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> fail(String msg, T data) {
        return build(ResponseEnum.FAIL.getCode(), msg, data);
    }

    public static <T> BaseResponse<T> fail(String msg) {
        return fail(msg, null);
    }
}