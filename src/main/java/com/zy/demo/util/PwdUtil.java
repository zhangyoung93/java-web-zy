package com.zy.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;

/**
 * 密码工具类
 *
 * @author zy
 */
@Slf4j
public class PwdUtil {

    static {
        //注册第三方密码算法库
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成SM4密钥
     *
     * @param dataType 数据类型
     * @return String
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     * @throws NoSuchProviderException  NoSuchProviderException
     */
    public static String generateSm4Key(String dataType) throws NoSuchAlgorithmException, NoSuchProviderException {
        //从第三方密码库中获取SM4的算法
        KeyGenerator keyGenerator = KeyGenerator.getInstance("SM4");
        //SM4密钥长度固定128位
        keyGenerator.init(128);
        //生成密钥
        SecretKey secretKey = keyGenerator.generateKey();
        //密钥转字节数组
        byte[] encodedSecretKey = secretKey.getEncoded();
        String sm4Key;
        switch (dataType) {
            case "Base64":
                sm4Key = bytesToBase64(encodedSecretKey);
                break;
            case "Hex":
                sm4Key = bytesToHex(encodedSecretKey);
                break;
            default:
                throw new IllegalArgumentException("dataType not exists");
        }
        return sm4Key;
    }

    /**
     * SHA-256哈希
     *
     * @param text 原文
     * @return String
     */
    public static String sha256Digest(String text) throws NoSuchAlgorithmException {
        Assert.hasText(text, "text must not be empty");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes);
    }

    /**
     * 字节数组转换Base64字符串
     *
     * @param bytes 字节数组
     * @return String
     */
    public static String bytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 字节数组转换十六进制小写字符串
     *
     * @param bytes 字节数组
     * @return String
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
