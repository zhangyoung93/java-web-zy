package com.zy.demo.model;

import java.io.Serializable;

/**
 * zy_user_job
 * @author 
 */
public class ZyUserJob implements Serializable {
    /**
     * 任职ID
     */
    private Long jobId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 任职名称
     */
    private String jobName;

    private static final long serialVersionUID = 1L;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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
}