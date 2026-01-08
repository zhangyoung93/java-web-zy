package com.zy.demo.model;

import java.io.Serializable;

/**
 * zy_user_id_card
 * @author zy
 */
public class ZyUserIdCard implements Serializable {
    /**
     * 身份证ID
     */
    private Long cardId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 身份证号
     */
    private String idCardNo;

    private static final long serialVersionUID = 1L;

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
}