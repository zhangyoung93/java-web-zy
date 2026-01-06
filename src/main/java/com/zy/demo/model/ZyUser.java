package com.zy.demo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * zy_user
 *
 * @author zy
 */
public class ZyUser implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userFullName;

    /**
     * 登录用户名
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPwd;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 身份证信息
     */
    private ZyUserIdCard zyUserIdCard;

    private List<ZyUserJob> zyUserJobList;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
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

    public ZyUserIdCard getZyUserIdCard() {
        return zyUserIdCard;
    }

    public void setZyUserIdCard(ZyUserIdCard zyUserIdCard) {
        this.zyUserIdCard = zyUserIdCard;
    }

    public List<ZyUserJob> getZyUserJobList() {
        return zyUserJobList;
    }

    public void setZyUserJobList(List<ZyUserJob> zyUserJobList) {
        this.zyUserJobList = zyUserJobList;
    }
}