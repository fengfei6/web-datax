package com.feng.project.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.feng.project.domain.User;
import com.feng.project.util.OracleUtil;
import com.feng.project.util.PostgreSqlUtil;
import com.feng.project.util.SqlServerUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Datasource;
import com.feng.project.service.DatasourceService;
import com.feng.project.util.MysqlUtil;

@Controller
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;

    @RequestMapping("/datasource/add")
    public ModelAndView saveDB(Datasource datasource, HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        if(datasource.getId()==null) {
	        datasource.setUserId(user.getId());
	        datasource.setIsConnection("0");
            datasourceService.save(datasource);
        }else {
            datasourceService.update(datasource);
        }
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
        return new ModelAndView("admin/database-list","model",model);
    }

    @RequestMapping("/datasource/list")
    public ModelAndView findAll(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
        return new ModelAndView("admin/database-list","model",model);
    }

    @RequestMapping("/datasource/testConn/{id}")
    public ModelAndView testConn(@PathVariable String id,Model model,HttpSession session){
        Datasource datasource = datasourceService.getDatasource(Integer.parseInt(id));
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
        if(flag) {
        	datasource.setIsConnection("1");
        }else {
        	datasource.setIsConnection("0");
        }
        datasourceService.update(datasource);
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
        model.addAttribute("conn_flag", flag);
        return new ModelAndView("admin/database-list","model",model);
    }
    
    @RequestMapping("/datasource/edit/{id}")
    public ModelAndView getOneDatesource(@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
    	return new ModelAndView("admin/edit-database","datasource",datasource);
    }
    
    @RequestMapping("/datasource/delete/{id}")
    public ModelAndView deleteDatesource(@PathVariable Integer id,Model model,HttpSession session) {
        datasourceService.delete(id);
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
    	return new ModelAndView("admin/database-list","model",model);
    }
    
    @RequestMapping("/datasource/showAll/{id}")
    public ModelAndView showAllTables(@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
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
    	model.addAttribute("id",id);
    	model.addAttribute("tablemap", map);
    	return new ModelAndView("admin/show-table","model",model);
    }
    
    @RequestMapping("/datasource/deleteTable/{id}/{tableName}")
    public ModelAndView deleteTable(@PathVariable String tableName,@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
        Connection conn = null;
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
        	conn = SqlServerUtil.getConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
        	conn = PostgreSqlUtil.getConn(datasource);
        }
    	MysqlUtil.executeSQL(conn, "drop table "+tableName);
        Map<String, Integer> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getTables(conn);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getTables(conn,datasource.getUsername());
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getTables(conn,datasource.getDbname());
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getTables(conn);
        }
    	model.addAttribute("id",id);
    	model.addAttribute("tablemap", map);
    	return new ModelAndView("admin/show-table","model",model);
    }
    
    @RequestMapping("/datasource/showTableDesc/{id}/{name}")
    public ModelAndView showTableDesc(@PathVariable String name,@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
        Connection conn = null;
        Map<String, String> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getColumn(MysqlUtil.getMetaDate(conn), name);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getColumn(conn,name);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getColumn(conn,name);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getColumn(conn, name);
        }
    	model.addAttribute("table", map);
    	model.addAttribute("tableName", name);
    	return new ModelAndView("admin/show-table-desc","model",model);
    }

    @RequestMapping("/datasource/getTableColumn")
    @ResponseBody
    public String showTableColumn(String name,Integer id) {
        Datasource datasource = datasourceService.getDatasource(id);
        Connection conn = null;
        Map<String, String> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getColumn(MysqlUtil.getMetaDate(conn), name);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getColumn(conn,name);
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getColumn(conn,name);
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getColumn(conn, name);
        }
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(String key:map.keySet()){
            count++;
            if(count == map.keySet().size()){
                sb.append(key);
            }else{
                sb.append(key+",");
            }
        }
        return sb.toString();
    }

    @RequestMapping("/datasource/copyTableForm")
    public ModelAndView copyTableForm(Model model,HttpSession session) {
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        List<Datasource> list_part = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        if(user.getRole().equalsIgnoreCase("admin")) {
            list_part = datasourceService.getDatasourcesByType("POSTGRESQL");
        }else if(user.getRole().equalsIgnoreCase("user")){
            list_part = datasourceService.getDatasourcesByTypeAndUserId("POSTGRESQL",user.getId());
        }
        model.addAttribute("datalist", list);
        model.addAttribute("postgre", list_part);
        return new ModelAndView("admin/copy-table","model",model);
    }

    @ResponseBody
    @RequestMapping("/datasource/getTableList")
    public Map<String,Integer> getTableList(String name,Integer id) {
    	Datasource datasource = datasourceService.findDatasourceByNameAndUserId(name, id);
        Connection conn = null;
        Map<String, Integer> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getTables(conn);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getTables(conn,datasource.getUsername());
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getTables(conn,datasource.getDbname());
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getTables(conn);
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/datasource/getTableList2")
    public Map<String,Integer> getTableList2(Integer id) {
    	Datasource datasource = datasourceService.getDatasource(id);
        Connection conn = null;
        Map<String, Integer> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getTables(conn);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getTables(conn,datasource.getUsername());
        }else if(datasource.getType().equalsIgnoreCase("sqlserver")){
            conn = SqlServerUtil.getConn(datasource);
            map = SqlServerUtil.getTables(conn,datasource.getDbname());
        }else if(datasource.getType().equalsIgnoreCase("postgresql")){
            conn = PostgreSqlUtil.getConn(datasource);
            map = PostgreSqlUtil.getTables(conn);
        }
        return map;
    }
    
    @RequestMapping("/datasource/copyTable")
    public ModelAndView copyTable(Model model,String sdatasource,String sname,String ddatasource,String dname,HttpSession session) {
    	Datasource datasources = datasourceService.findDatasourceByName(sdatasource);
    	Datasource datasourced = datasourceService.findDatasourceByName(ddatasource);
        Connection conn = null;
        String createsql = "";
        if(datasources.getType().equalsIgnoreCase("mysql")) {
            conn = MysqlUtil.getConn(datasources);
            createsql = MysqlUtil.createTable(
                    MysqlUtil.getPrimaryKey(MysqlUtil.getMetaDate(conn), sname),
                    dname, MysqlUtil.getColumn(MysqlUtil.getMetaDate(conn), sname));
        }else if(datasources.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasources);
            createsql = OracleUtil.createTable(
                    OracleUtil.getPrimaryKey(conn, sname),
                    dname, OracleUtil.getColumn(conn, sname));
        }else if(datasources.getType().equalsIgnoreCase("sqlserver")) {
            conn = SqlServerUtil.getConn(datasources);
            createsql = SqlServerUtil.createTable(
            		SqlServerUtil.getPrimaryKey(conn, sname),
                    dname, SqlServerUtil.getColumn(conn, sname));
        }else if(datasources.getType().equalsIgnoreCase("postgresql")){
            conn = PostgreSqlUtil.getConn(datasources);
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
            conn = PostgreSqlUtil.getConn(datasourced);
            PostgreSqlUtil.executeSQL(conn, createsql);
        }
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
    	return new ModelAndView("admin/database-list","model",model);
    }

    @ResponseBody
    @RequestMapping("/datasource/getDataSourceByName")
    public String getDataSourceByName(String name,HttpSession session){
    	User user = (User)session.getAttribute("user");
    	if(user.getRole().equalsIgnoreCase("admin")) {
	        if(datasourceService.findDatasourceByName(name) != null){
	            return "名称已存在，重新输入";
	        }else{
	            return "";
	        }
        }else {
        	if(datasourceService.getDatasourceByDbnameAndUserId(name,user.getId()) != null){
	            return "名称已存在，重新输入";
	        }else{
	            return "";
	        }
        }
    }

    @RequestMapping("/datasource/search")
    public ModelAndView search(String name,String type,Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(type.equalsIgnoreCase("all")){
            if(user.getRole().equalsIgnoreCase("admin")) {
                list = datasourceService.findDatasourcesByName(name);
            }else if(user.getRole().equalsIgnoreCase("user")){
                list = datasourceService.findDatasourcesByNameAndUserId(name,user.getId());
            }
        }else {
            if(user.getRole().equalsIgnoreCase("admin")) {
                list = datasourceService.findDatasourcesByTypeAndName(type, name);
            }else if(user.getRole().equalsIgnoreCase("user")){
                list = datasourceService.findDatasourcesByTypeAndNameAndUserId(type,name,user.getId());
            }
        }
        model.addAttribute("datalist", list);
        return new ModelAndView("admin/database-list","model",model);
    }
    
    @RequestMapping("/datasource/showTableData/{id}/{tableName}")
    public ModelAndView getTableDate(@PathVariable Integer id,@PathVariable String tableName,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
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
    	model.addAttribute("result", result);
    	return new ModelAndView("admin/show-table-data","model",model);
    }
}
