package com.zy.demo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * id_user_base
 * @author 
 */
public class IdUserBase implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 性别
     */
    private Boolean sex;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户密码
     */
    private IdUserPwd idUserPwd;

    /**
     * 用户任职信息
     */
    private List<IdUserJob> idUserJobList;

    private static final long serialVersionUID = 1L;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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

    public IdUserPwd getIdUserPwd() {
        return idUserPwd;
    }

    public void setIdUserPwd(IdUserPwd idUserPwd) {
        this.idUserPwd = idUserPwd;
    }

    public List<IdUserJob> getIdUserJobList() {
        return idUserJobList;
    }

    public void setIdUserJobList(List<IdUserJob> idUserJobList) {
        this.idUserJobList = idUserJobList;
    }
}