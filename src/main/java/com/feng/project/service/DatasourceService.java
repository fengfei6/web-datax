package com.feng.project.service;

import com.feng.project.domain.Datasource;
import com.feng.project.repository.DatasourceRepository;
import com.feng.project.util.MysqlUtil;
import com.feng.project.util.OracleUtil;
import com.feng.project.util.PostgreSqlUtil;
import com.feng.project.util.SqlServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DatasourceService {
    @Autowired
    private DatasourceRepository datasourceRepository;

    public void save(Datasource datasource){
        datasource.setUpdateTime(new java.sql.Date(System.currentTimeMillis()));
        datasource.setIsConnection("0");
        datasourceRepository.save(datasource);
    }

    public boolean testConnection(Datasource datasource){
        boolean flag = false;
        if(datasource.getType().equalsIgnoreCase("mysql")){
            flag = MysqlUtil.isConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            flag = OracleUtil.isConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            flag = SqlServerUtil.isConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            flag = PostgreSqlUtil.isConn(datasource);
        }
        return flag;
    }

    public List<Datasource> findAll(){
        return datasourceRepository.findAll();
    }

    public Datasource getDatasource(Integer id){
        return datasourceRepository.getOne(id);
    }
    
    public void update(Datasource datasource) {
    	datasource.setUpdateTime(new Date(System.currentTimeMillis()));
    	datasourceRepository.save(datasource);
    }
    
    public void delete(Integer id) {
    	datasourceRepository.deleteById(id);
    }

    public Map<String, Integer> getAllTables(Datasource datasource){
        Map<String, Integer> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            Connection conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getTables(conn);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            Connection conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getTables(conn,datasource.getUsername());
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            Connection conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getTables(conn,datasource.getDbname());
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            Connection conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getTables(conn);
        }
        return map;
    }


    public void dropTable(Datasource datasource,String tableName){
        if(datasource.getType().equalsIgnoreCase("mysql")){
            Connection conn = MysqlUtil.getConn(datasource);
            MysqlUtil.executeSQL(conn, "drop table "+tableName);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            Connection conn = OracleUtil.getConn(datasource);
            OracleUtil.executeSQL(conn, "drop table "+tableName);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            Connection conn = SqlServerUtil.getConn(datasource);
            SqlServerUtil.executeSQL(conn, "drop table "+tableName);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            Connection conn = PostgreSqlUtil.getConn(datasource);
            PostgreSqlUtil.executeSQL(conn, "drop table "+tableName);
        }
    }


    public Map<String, String> getTableColumns(Datasource datasource,String name){
        Map<String, String> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            Connection conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getColumn(MysqlUtil.getMetaDate(conn), name);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            Connection conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getColumn(conn,name);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            Connection conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getColumn(conn,name);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            Connection conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getColumn(conn, name);
        }
        return map;
    }

    public void copyTable(Datasource datasources,String sname,Datasource datasourced,String dname){
        String createsql = "";
        if(datasources.getType().equalsIgnoreCase("mysql")) {
            Connection conn = MysqlUtil.getConn(datasources);
            createsql = MysqlUtil.createTable(
                    MysqlUtil.getPrimaryKey(MysqlUtil.getMetaDate(conn), sname),
                    dname, MysqlUtil.getColumn(MysqlUtil.getMetaDate(conn), sname));
        }else if(datasources.getType().equalsIgnoreCase("oracle")){
            Connection conn = OracleUtil.getConn(datasources);
            createsql = OracleUtil.createTable(
                    OracleUtil.getPrimaryKey(conn, sname),
                    dname, OracleUtil.getColumn(conn, sname));
        }else if(datasources.getType().equalsIgnoreCase("sqlserver")) {
            Connection conn = SqlServerUtil.getConn(datasources);
            createsql = SqlServerUtil.createTable(
                    SqlServerUtil.getPrimaryKey(conn, sname),
                    dname, SqlServerUtil.getColumn(conn, sname));
        }else if(datasources.getType().equalsIgnoreCase("postgresql")){
            Connection conn = PostgreSqlUtil.getConn(datasources);
            createsql = PostgreSqlUtil.createTable(
                    PostgreSqlUtil.getPrimaryKey(conn, sname),
                    dname, PostgreSqlUtil.getColumn(conn, sname));
        }
        if(datasourced.getType().equalsIgnoreCase("postgresql")){
            if(datasources.getType().equalsIgnoreCase("oracle")){
                createsql=createsql.replace("VARCHAR2(255)","varchar(255)").replace("NUMBER","int");
            }
            if(datasources.getType().equalsIgnoreCase("mysql")){
                createsql = createsql.replace("DATETIME","DATE").replace("LONGTEXT","TEXT");
            }
            if(datasources.getType().equalsIgnoreCase("sqlserver")){
                createsql = createsql.replace("datetime","date").replace("longtext","text");
            }
            Connection conn = PostgreSqlUtil.getConn(datasourced);
            PostgreSqlUtil.executeSQL(conn, createsql);
        }
    }

    public List<List<Object>> getTableData(Datasource datasource,String tableName){
        List<List<Object>> result = new ArrayList<>();
        if(datasource.getType().equalsIgnoreCase("mysql")) {
            result = MysqlUtil.getTableData(MysqlUtil.getConn(datasource), tableName);
        }else if(datasource.getType().equalsIgnoreCase("oracle")) {
            result = OracleUtil.getTableData(OracleUtil.getConn(datasource), tableName);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")) {
            result = SqlServerUtil.getTableData(SqlServerUtil.getConn(datasource), tableName);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")) {
            result = PostgreSqlUtil.getTableData(PostgreSqlUtil.getConn(datasource), tableName);
        }
        return result;
    }

    public Datasource findDatasourceByName(String name) {
    	return datasourceRepository.findDatasourceByName(name);
    }

    public List<Datasource> findDatasourcesByTypeAndName(String type,String name){
        return datasourceRepository.findDatasourcesByNameAndType(type,name);
    }

    public List<Datasource> findDatasourcesByName(String name){
        return datasourceRepository.findDatasourcesByName(name);
    }

    public List<Datasource> findDatasourcesByTypeAndNameAndUserId(String type,String name,Integer userId){
        return datasourceRepository.findDatasourcesByNameAndTypeAndUserId(type,name,userId);
    }

    public List<Datasource> findDatasourcesByNameAndUserId(String name,Integer userId){
        return datasourceRepository.findDatasourcesByNameAndUserId(name,userId);
    }


    public List<Datasource> findDatasourcesByUserId(Integer userId){
        return datasourceRepository.findDatasourcesByUserId(userId);
    }
    
    public Datasource getDatasourceByDbnameAndUserId(String dbname,Integer userId){
        return datasourceRepository.getDatasourceByDbnameAndUserId(dbname,userId);
    }

    public List<Datasource> getDatasourcesByTypeAndUserId(String type,Integer userId){
        return datasourceRepository.getDatasourcesByTypeAndUserId(type,userId);
    }

    public List<Datasource> getDatasourcesByType(String type){
        return datasourceRepository.getDatasourcesByType(type);
    }
}
