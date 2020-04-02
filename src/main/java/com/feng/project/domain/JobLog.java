package com.feng.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name="job_log")
public class JobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer jobId;
    private String jobName;
    private String logResult;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

    public String getLogResult() {
        return logResult;
    }

    public void setLogResult(String logResult) {
        this.logResult = logResult;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public JobLog() {
    }

	public JobLog(Integer jobId, String jobName, String logResult, Date createTime) {
		this.jobId = jobId;
		this.jobName = jobName;
		this.logResult = logResult;
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "JobLog [id=" + id + ", jobId=" + jobId + ", jobName=" + jobName + ", logResult=" + logResult
				+ ", createTime=" + createTime + "]";
	}

    
}
