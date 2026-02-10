package com.zy.demo.util;

import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Sha256签名工具
 *
 * @author zy
 */
public class Sha256SignUtil {

    /**
     * 生成签名
     *
     * @param appNo     应用编码
     * @param timestamp 时间戳
     * @param bodyStr   请求体
     * @param secret    密钥
     * @return 签名数据
     * @throws Exception Exception
     */
    public static String generateSignData(String appNo, String timestamp, String bodyStr, String secret) throws Exception {
        Assert.hasText(appNo, "appNo must not be null");
        Assert.hasText(timestamp, "timestamp must not be null");
        Assert.hasText(bodyStr, "bodyStr must not be null");
        Assert.hasText(secret, "secret must not be null");
        String signData;
        StringBuilder sb = new StringBuilder();
        sb.append("appNo=").append(appNo).append("&timestamp=").append(timestamp).append("&bodyStr=").append(bodyStr).append(secret);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = messageDigest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
            signData = bytesToHex(bytes);
        } catch (Exception e) {
            throw new Exception("生成签名数据失败", e);
        }
        return signData;
    }

    /**
     * 验签
     *
     * @param appNo     应用编码
     * @param timestamp 时间戳
     * @param bodyStr   请求体
     * @param secret    密钥
     * @param signData  签名数据
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verifySignData(String appNo, String timestamp, String bodyStr, String secret, String signData) throws Exception {
        Assert.hasText(appNo, "appNo must not be null");
        Assert.hasText(timestamp, "timestamp must not be null");
        Assert.hasText(bodyStr, "bodyStr must not be null");
        Assert.hasText(secret, "secret must not be null");
        Assert.hasText(signData, "signData must not be null");
        String generateSignData;
        try {
            generateSignData = generateSignData(appNo, timestamp, bodyStr, secret);
        } catch (Exception e) {
            throw new Exception("验证签名失败", e);
        }
        return signData.equals(generateSignData);
    }

    /**
     * 字节流转十六进制字符串
     *
     * @param bytes 字节流
     * @return 字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexStr.append("0");
            }
            hexStr.append(hex);
        }
        return hexStr.toString();
    }
}
