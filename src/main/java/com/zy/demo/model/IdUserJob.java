package com.zy.demo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * id_user_job
 * @author 
 */
public class IdUserJob implements Serializable {
    /**
     * 用户任职ID
     */
    private Long userJobId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 任职名称
     */
    private String jobName;

    /**
     * 任职类型
     */
    private Integer jobType;

    /**
     * 任职状态
     */
    private String jobStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getUserJobId() {
        return userJobId;
    }

    public void setUserJobId(Long userJobId) {
        this.userJobId = userJobId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
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