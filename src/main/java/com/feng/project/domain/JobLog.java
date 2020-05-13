package com.feng.project.domain;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.sql.Date;
@Proxy(lazy = false)
@Entity(name="job_log")
public class JobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "job_id")
    private Integer jobId;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="log_result")
    private String logResult;

    @Column(name = "status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLogResult() {
        return logResult;
    }

    public void setLogResult(String logResult) {
        this.logResult = logResult;
    }

    public JobLog() {
    }

    public JobLog(String jobName, Integer jobId, Date createTime, String logResult,String status) {
        this.jobName = jobName;
        this.jobId = jobId;
        this.createTime = createTime;
        this.logResult = logResult;
        this.status = status;
    }

    @Override
    public String toString() {
        return "JobLog{" +
                "jobName='" + jobName + '\'' +
                ", jobId=" + jobId +
                ", createTime=" + createTime +
                ", logResult='" + logResult + '\'' +
                '}';
    }
}
