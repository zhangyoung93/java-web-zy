package com.zy.demo.pojo;

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

    public RedisMessageDto() {

    }

    public RedisMessageDto(Long bizId, int bizType, String bizContent, Date opTime) {
        this.bizId = bizId;
        this.bizType = bizType;
        this.bizContent = bizContent;
        this.opTime = opTime;
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
}
