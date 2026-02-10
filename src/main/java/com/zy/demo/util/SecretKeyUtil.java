package com.zy.demo.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * 密钥生成工具类
 *
 * @author zy
 */
public class SecretKeyUtil {

    /**
     * 生成签名密钥
     *
     * @return String
     */
    public static String generateSignKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
