package com.feng.project.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Datasource;
import com.feng.project.domain.User;
import com.feng.project.service.DatasourceService;
import com.feng.project.util.ConnectionUtil;

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
        boolean flag = ConnectionUtil.isConn(datasource,"mysql");
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
    	Connection conn = ConnectionUtil.getConn(datasource, datasource.getType());
    	model.addAttribute("id",id);
    	model.addAttribute("tablemap",ConnectionUtil.getTables(conn));
    	return new ModelAndView("admin/show-table","model",model);
    }
    
    @RequestMapping("/datasource/showTableDesc/{id}/{name}")
    public ModelAndView showTableDesc(@PathVariable String name,@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
    	Connection conn = ConnectionUtil.getConn(datasource, datasource.getType());
    	model.addAttribute("table",ConnectionUtil.getColumn(ConnectionUtil.getMetaDate(conn), name));
    	model.addAttribute("tableName", name);
    	model.addAttribute("primaryKeys",ConnectionUtil.getPrimaryKey(ConnectionUtil.getMetaDate(conn), name));
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
    	Connection conn = ConnectionUtil.getConn(datasource, datasource.getType());
        return ConnectionUtil.getTables(conn);
    }
    
    @ResponseBody
    @RequestMapping("/datasource/getTableList2")
    public Map<String,Integer> getTableList2(Integer id) {
    	Datasource datasource = datasourceService.getDatasource(id);
    	Connection conn = ConnectionUtil.getConn(datasource, datasource.getType());
        return ConnectionUtil.getTables(conn);
    }
    
    @RequestMapping("/datasource/copyTable")
    public ModelAndView copyTable(Model model,String sdatasource,String sname,String ddatasource,String dname) {
    	Datasource datasources = datasourceService.findDatasourceByName(sdatasource);
    	Datasource datasourced = datasourceService.findDatasourceByName(ddatasource);
    	Connection conn = ConnectionUtil.getConn(datasources, datasources.getType());
    	String createsql = ConnectionUtil.createTable(
    			ConnectionUtil.getPrimaryKey(ConnectionUtil.getMetaDate(conn), sname), 
    			dname, ConnectionUtil.getColumn(ConnectionUtil.getMetaDate(conn), sname));
    	conn = ConnectionUtil.getConn(datasourced, datasourced.getType());
    	boolean flag = ConnectionUtil.executeSQL(conn, createsql);
    	model.addAttribute("create_flag", flag);
    	model.addAttribute("datalist",datasourceService.findAll());
    	return new ModelAndView("admin/database-list","model",model);
    }
    
}
