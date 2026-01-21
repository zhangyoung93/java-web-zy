package com.zy.demo.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * jasypt加密工具
 * @author zy
 */
public class JasyptUtil {

    private static final String ALGORITHM = "PBEWithMD5AndDES";

    private static final String PASSWORD = "zySecret";

    public static void main(String[] args) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm(ALGORITHM);
        config.setPassword(PASSWORD);
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        encryptor.setConfig(config);
        System.out.println("encrypt="+encryptor.encrypt("root"));
        System.out.println("decrypt="+encryptor.decrypt("j7ZVVj7tm1eohi0Ydzbwrg=="));
    }

}
