package com.feng.project.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name="job_log")
public class JobLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "executor_address")
    private String executorAddress;

    @Column(name = "executor_param")
    private String executorParam;

    @Column(name = "trigger_time")
    private String triggerTime;

    @Column(name = "trigger_code")
    private Integer triggerCode;

    @Column(name = "trigger_msg")
    private String triggerMsg;

    @Column(name = "handle_time")
    private String handleTime;

    @Column(name = "handle_code")
    private int handleCode;

    @Column(name = "handle_msg")
    private String handleMsg;

    @Column(name = "execid")
    private String execid;

    @Column(name = "job_id")
    private Integer jobId;

    private Integer userId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public Integer getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(Integer triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getTriggerMsg() {
        return triggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public int getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(int handleCode) {
        this.handleCode = handleCode;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public String getExecid() {
        return execid;
    }

    public void setExecid(String execid) {
        this.execid = execid;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public JobLog() {
    }

    public JobLog(String name, String status, Integer taskId, String executorAddress, String executorParam, String triggerTime, Integer triggerCode, String triggerMsg, String handleTime, int handleCode, String handleMsg, String execid, Integer jobId, Integer userId) {
        this.name = name;
        this.status = status;
        this.taskId = taskId;
        this.executorAddress = executorAddress;
        this.executorParam = executorParam;
        this.triggerTime = triggerTime;
        this.triggerCode = triggerCode;
        this.triggerMsg = triggerMsg;
        this.handleTime = handleTime;
        this.handleCode = handleCode;
        this.handleMsg = handleMsg;
        this.execid = execid;
        this.jobId = jobId;
        this.userId = userId;
    }
}
