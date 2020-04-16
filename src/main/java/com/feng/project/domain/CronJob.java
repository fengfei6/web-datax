package com.feng.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity(name="cron_job")
public class CronJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String querySql;
    private Integer readerDbId;
    private String readerDbType;
    private String readerTable;
    private String readerColumn;
    private Integer writerDbId;
    private String writerDbType;
    private String writerTable;
    private String writerColumn;
    private String writerPresql;
    private String writerPostsql;
    private Integer isRunning;
    private String cronExpress;
    private String jsonContent;
    private Date createTime;
    private Date updateTime;
    private Integer userId;
    private Integer taskId;

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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public Integer getReaderDbId() {
        return readerDbId;
    }

    public void setReaderDbId(Integer readerDbId) {
        this.readerDbId = readerDbId;
    }

    public String getReaderTable() {
        return readerTable;
    }

    public void setReaderTable(String readerTable) {
        this.readerTable = readerTable;
    }

    public String getReaderColumn() {
        return readerColumn;
    }

    public void setReaderColumn(String readerColumn) {
        this.readerColumn = readerColumn;
    }

    public Integer getWriterDbId() {
        return writerDbId;
    }

    public void setWriterDbId(Integer writerDbId) {
        this.writerDbId = writerDbId;
    }

    public String getWriterTable() {
        return writerTable;
    }

    public void setWriterTable(String writerTable) {
        this.writerTable = writerTable;
    }

    public String getWriterColumn() {
        return writerColumn;
    }

    public void setWriterColumn(String writerColumn) {
        this.writerColumn = writerColumn;
    }

    public String getWriterPresql() {
        return writerPresql;
    }

    public void setWriterPresql(String writerPresql) {
        this.writerPresql = writerPresql;
    }

    public String getWriterPostsql() {
        return writerPostsql;
    }

    public void setWriterPostsql(String writerPostsql) {
        this.writerPostsql = writerPostsql;
    }

    public Integer getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(Integer isRunning) {
        this.isRunning = isRunning;
    }

    public String getCronExpress() {
        return cronExpress;
    }

    public void setCronExpress(String cronExpress) {
        this.cronExpress = cronExpress;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public CronJob() {
    }

    public CronJob(String name, String description, String querySql, Integer readerDbId, String readerTable, String readerColumn, Integer writerDbId, String writerTable, String writerColumn, String writerPresql, String writerPostsql, String cronExpress, String jsonContent, Date createTime, Date updateTime, Integer userId) {
        this.name = name;
        this.description = description;
        this.querySql = querySql;
        this.readerDbId = readerDbId;
        this.readerTable = readerTable;
        this.readerColumn = readerColumn;
        this.writerDbId = writerDbId;
        this.writerTable = writerTable;
        this.writerColumn = writerColumn;
        this.writerPresql = writerPresql;
        this.writerPostsql = writerPostsql;
        this.cronExpress = cronExpress;
        this.jsonContent = jsonContent;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", querySql='" + querySql + '\'' +
                ", readerDbId=" + readerDbId +
                ", readerTable='" + readerTable + '\'' +
                ", readerColumn='" + readerColumn + '\'' +
                ", writerDbId=" + writerDbId +
                ", writerTable='" + writerTable + '\'' +
                ", writerColumn='" + writerColumn + '\'' +
                ", writerPresql='" + writerPresql + '\'' +
                ", writerPostsql='" + writerPostsql + '\'' +
                ", cronExpress='" + cronExpress + '\'' +
                ", jsonContent='" + jsonContent + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                '}';
    }
}
