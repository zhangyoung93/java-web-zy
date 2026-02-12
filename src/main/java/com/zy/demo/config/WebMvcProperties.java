package com.zy.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

/**
 * Web MVC 属性
 *
 * @author zy
 */
@RefreshScope
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

        private List<IdempotentRule> idempotentRuleList;

        public int getValidTime() {
            return validTime;
        }

        public void setValidTime(int validTime) {
            this.validTime = validTime;
        }

        public List<IdempotentRule> getIdempotentRuleList() {
            return idempotentRuleList;
        }

        public void setIdempotentRuleList(List<IdempotentRule> idempotentRuleList) {
            this.idempotentRuleList = idempotentRuleList;
        }
    }

    /**
     * 幂等规则
     */
    public static class IdempotentRule {

        String path;

        String field;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}
