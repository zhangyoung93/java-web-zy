package com.zy.demo.util;

import com.ulisesbocchio.jasyptspringboot.properties.JasyptEncryptorConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.springframework.util.Assert;

/**
 * jasypt加密工具
 *
 * @author zy
 */
@Slf4j
public class JasyptUtil {

    private final StandardPBEStringEncryptor encryptor;

    public JasyptUtil(JasyptEncryptorConfigurationProperties jasyptEncryptorConfigurationProperties) {
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm(jasyptEncryptorConfigurationProperties.getAlgorithm());
        config.setPassword(jasyptEncryptorConfigurationProperties.getPassword());
        config.setIvGeneratorClassName(jasyptEncryptorConfigurationProperties.getIvGeneratorClassname());
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);
    }

    /**
     * 加密
     *
     * @param plaintext 明文
     * @return 密文
     */
    public String encrypt(String plaintext) {
        Assert.hasText(plaintext, "plaintext must not be null");
        String encryptStr = this.encryptor.encrypt(plaintext);
        log.info("encryptStr={}", encryptStr);
        return encryptStr;
    }

    /**
     * 解密
     *
     * @param encryptStr 密文
     * @return 明文
     */
    public String decrypt(String encryptStr) {
        Assert.hasText(encryptStr, "encryptStr must not be null");
        String decryptStr = this.encryptor.decrypt(encryptStr);
        log.info("decryptStr={}", decryptStr);
        return decryptStr;
    }
}
