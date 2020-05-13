package com.feng.project.domain;

import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
@Proxy(lazy = false)
@Entity(name="job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String readerDbType;
    private String writerDbType;
    private String jsonContent;
    private Date createTime;
    private Integer userId;
    private Integer taskId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReaderDbType() {
        return readerDbType;
    }

    public void setReaderDbType(String readerDbType) {
        this.readerDbType = readerDbType;
    }

    public String getWriterDbType() {
        return writerDbType;
    }

    public void setWriterDbType(String writerDbType) {
        this.writerDbType = writerDbType;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", readerDbType='" + readerDbType + '\'' +
                ", writerDbType='" + writerDbType + '\'' +
                ", jsonContent='" + jsonContent + '\'' +
                ", createTime=" + createTime +
                ", userId=" + userId +
                '}';
    }

    public Job() {
    }

    public Job(String name, String description, String readerDbType, String writerDbType, String jsonContent, Date createTime, Integer userId) {
        this.name = name;
        this.description = description;
        this.readerDbType = readerDbType;
        this.writerDbType = writerDbType;
        this.jsonContent = jsonContent;
        this.createTime = createTime;
        this.userId = userId;
    }
}
