package com.zy.demo.constant;

/**
 * 应答枚举
 *
 * @author zy
 */
public enum ResponseEnum {
    /**
     * 编码+描述
     */
    SUCCESS("0000", "处理成功！"),
    FAIL("1000", "处理失败！"),
    EXCEPTION("-1000", "程序异常，请联系管理员！"),
    SIGN_FAIL("1001", "接口验签不通过！"),
    IDEMPOTENT_FAIL("1002", "幂等校验不通过！");

    private final String code;

    private final String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
