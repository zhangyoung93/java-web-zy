package com.zy.demo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * id_user_pwd
 * @author 
 */
public class IdUserPwd implements Serializable {
    /**
     * 用户密码ID
     */
    private Long userPwdId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户密码状态
     */
    private String userPwdStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getUserPwdId() {
        return userPwdId;
    }

    public void setUserPwdId(Long userPwdId) {
        this.userPwdId = userPwdId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPwdStatus() {
        return userPwdStatus;
    }

    public void setUserPwdStatus(String userPwdStatus) {
        this.userPwdStatus = userPwdStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}