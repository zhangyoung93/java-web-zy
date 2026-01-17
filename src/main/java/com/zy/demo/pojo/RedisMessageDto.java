package com.zy.demo.pojo;

import org.springframework.util.Assert;

import java.util.Date;

/**
 * redis消息DTO
 *
 * @author zy
 */
public class RedisMessageDto {

    private Long bizId;

    private int bizType;

    private String bizContent;

    private Date opTime;

    private RedisMessageDto() {

    }

    private RedisMessageDto(Builder builder) {
        this.bizId = builder.bizId;
        this.bizType = builder.bizType;
        this.bizContent = builder.bizContent;
        this.opTime = builder.opTime;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }

    public Date getOpTime() {
        return opTime;
    }

    public void setOpTime(Date opTime) {
        this.opTime = opTime;
    }

    @Override
    public String toString() {
        return "RedisMessageDto{" +
                "bizId=" + bizId +
                ", bizType=" + bizType +
                ", bizContent='" + bizContent + '\'' +
                ", opTime=" + opTime +
                '}';
    }

    /**
     * 建造者
     */
    public static class Builder {

        private Long bizId;

        private int bizType;

        private String bizContent;

        private Date opTime = new Date();

        public Builder bizId(Long bizId) {
            this.bizId = bizId;
            return this;
        }

        public Builder bizType(int bizType) {
            this.bizType = bizType;
            return this;
        }

        public Builder opTime(Date opTime) {
            this.opTime = opTime;
            return this;
        }

        public Builder bizContent(String bizContent) {
            this.bizContent = bizContent;
            return this;
        }

        public RedisMessageDto build() {
            Assert.notNull(this.bizId, "bizId must not be null!");
            return new RedisMessageDto(this);
        }
    }

    /**
     * 对外只提供建造者
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }
}
