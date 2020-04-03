package com.feng.project.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feng.project.util.OracleUtil;
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
    public ModelAndView saveDB(Datasource datasource, HttpServletRequest request,Model model){
        //User user = (User)request.getSession().getAttribute("user");
        if(datasource.getId()==null) {
	        //datasource.setUserId(user.getId());
	        datasource.setIsConnection("0");
            datasourceService.save(datasource);
        }else {
            datasourceService.update(datasource);
        }
        model.addAttribute("datalist",datasourceService.findAll());
        return new ModelAndView("admin/database-list","model",model);
    }

    @RequestMapping("/datasource/list")
    public ModelAndView findAll(Model model){
    	model.addAttribute("datalist",datasourceService.findAll());
        return new ModelAndView("admin/database-list","model",model);
    }

    @RequestMapping("/datasource/testConn/{id}")
    public ModelAndView testConn(HttpServletRequest request, HttpServletResponse response, @PathVariable String id,Model model){
        Datasource datasource = datasourceService.getDatasource(Integer.parseInt(id));
        boolean flag = false;
        if(datasource.getType().equalsIgnoreCase("mysql")){
            flag = MysqlUtil.isConn(datasource);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            flag = OracleUtil.isConn(datasource);
        }
        if(flag) {
        	datasource.setIsConnection("1");
        }else {
        	datasource.setIsConnection("0");
        }
        datasourceService.update(datasource);
        model.addAttribute("datalist", datasourceService.findAll());
        model.addAttribute("conn_flag", flag);
        return new ModelAndView("admin/database-list","model",model);
    }
    
    @RequestMapping("/datasource/edit/{id}")
    public ModelAndView getOneDatesource(@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
    	return new ModelAndView("admin/edit-database","datasource",datasource);
    }
    
    @RequestMapping("/datasource/delete/{id}")
    public ModelAndView deleteDatesource(@PathVariable Integer id,Model model) {
        datasourceService.delete(id);
    	model.addAttribute("datalist", datasourceService.findAll());
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
        }
    	MysqlUtil.executeSQL(conn, "drop table "+tableName);
        Map<String, Integer> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getTables(conn);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getTables(conn,datasource.getUsername());
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
        }
    	model.addAttribute("table", map);
    	model.addAttribute("tableName", name);
    	//model.addAttribute("primaryKeys", MysqlUtil.getPrimaryKey(MysqlUtil.getMetaDate(conn), name));
    	return new ModelAndView("admin/show-table-desc","model",model);
    }
    
    @RequestMapping("/datasource/copyTableForm")
    public ModelAndView copyTableForm(Model model) {
    	model.addAttribute("datalist",datasourceService.findAll());
        return new ModelAndView("admin/copy-table","model",model);
    }
    
    @ResponseBody
    @RequestMapping("/datasource/getTableList")
    public Map<String,Integer> getTableList(String name) {
    	Datasource datasource = datasourceService.findDatasourceByName(name);
        Connection conn = null;
        Map<String, Integer> map = new HashMap<>();
        if(datasource.getType().equalsIgnoreCase("mysql")){
            conn = MysqlUtil.getConn(datasource);
            map = MysqlUtil.getTables(conn);
        }else if(datasource.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasource);
            map = OracleUtil.getTables(conn,datasource.getUsername());
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
        }
        return map;
    }
    
    @RequestMapping("/datasource/copyTable")
    public ModelAndView copyTable(Model model,String sdatasource,String sname,String ddatasource,String dname) {
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
        }
        if(datasourced.getType().equalsIgnoreCase("mysql")) {
            conn = MysqlUtil.getConn(datasourced);
            MysqlUtil.executeSQL(conn, createsql);
        }else if(datasourced.getType().equalsIgnoreCase("oracle")){
            conn = OracleUtil.getConn(datasourced);
            OracleUtil.executeSQL(conn,createsql);
        }
    	model.addAttribute("datalist",datasourceService.findAll());
    	return new ModelAndView("admin/database-list","model",model);
    }

    @ResponseBody
    @RequestMapping("/datasource/getDataSourceByName")
    public String getDataSourceByName(String name){
        if(datasourceService.findDatasourceByName(name) != null){
            return "名称已存在，重新输入";
        }else{
            return "";
        }
    }
}
