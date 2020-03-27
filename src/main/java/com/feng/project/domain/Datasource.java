package com.feng.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author 小可爱
 */
@Entity(name="datasource")
public class Datasource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String ip;
    private String port;
    private String username;
    private String password;
    private String dbname;
    private String isConnection;
    private String type;
    private Date createTime;
    private Date updateTime;
    private Integer userId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getIsConnection() {
        return isConnection;
    }

    public void setIsConnection(String isConnection) {
        this.isConnection = isConnection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Datasource() {
    }

    public Datasource(Integer id,String name, String description, String ip, String port, String username, String password, String dbname, String isConnection, String type, Date createTime, Date updateTime, Integer userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.dbname = dbname;
        this.isConnection = isConnection;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.userId = userId;
    }

    public Datasource(String ip, String port,String dbname,String username,String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.dbname = dbname;
    }

    @Override
    public String toString() {
        return "Datasource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dbname='" + dbname + '\'' +
                ", isConnection='" + isConnection + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                '}';
    }
}
