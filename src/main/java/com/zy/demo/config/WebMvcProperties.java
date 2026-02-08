package com.zy.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Web MVC 属性
 *
 * @author zy
 */
@ConfigurationProperties(prefix = "web.mvc")
public class WebMvcProperties {

    private Sign sign;

    private Idempotent idempotent;

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Idempotent getIdempotent() {
        return idempotent;
    }

    public void setIdempotent(Idempotent idempotent) {
        this.idempotent = idempotent;
    }

    public static class Sign {

        private String secret;

        private List<String> includePaths;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public List<String> getIncludePaths() {
            return includePaths;
        }

        public void setIncludePaths(List<String> includePaths) {
            this.includePaths = includePaths;
        }
    }

    public static class Idempotent {

        private int validTime = 3600;

        private Map<String, String> includePaths;

        public int getValidTime() {
            return validTime;
        }

        public void setValidTime(int validTime) {
            this.validTime = validTime;
        }

        public Map<String, String> getIncludePaths() {
            return includePaths;
        }

        public void setIncludePaths(Map<String, String> includePaths) {
            this.includePaths = includePaths;
        }
    }
}
