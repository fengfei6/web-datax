package com.feng.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.feng.project.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Datasource;
import com.feng.project.service.DatasourceService;

@Controller
public class DatasourceController {
    @Autowired
    private DatasourceService datasourceService;

    @RequestMapping("/datasource/add")
    public ModelAndView saveDB(Datasource datasource, HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        if(datasource.getId()==null) {
	        datasource.setUserId(user.getId());
            datasourceService.save(datasource);
        }else {
            datasourceService.update(datasource);
        }
        return findAll(model,session);
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
        boolean flag = datasourceService.testConnection(datasource);
        if(flag) {
        	datasource.setIsConnection("1");
        }else {
        	datasource.setIsConnection("0");
        }
        datasourceService.update(datasource);
        model.addAttribute("conn_flag", flag);
       return findAll(model, session);
    }
    
    @RequestMapping("/datasource/edit/{id}")
    public ModelAndView getOneDatesource(@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
    	return new ModelAndView("admin/edit-database","datasource",datasource);
    }
    
    @RequestMapping("/datasource/delete/{id}")
    public ModelAndView deleteDatesource(@PathVariable Integer id,Model model,HttpSession session) {
        datasourceService.delete(id);
        return findAll(model, session);
    }
    
    @RequestMapping("/datasource/showAll/{id}")
    public ModelAndView showAllTables(@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
        Map<String, Integer> map = datasourceService.getAllTables(datasource);
    	model.addAttribute("id",id);
    	model.addAttribute("tablemap", map);
    	return new ModelAndView("admin/show-table","model",model);
    }
    
    @RequestMapping("/datasource/deleteTable/{id}/{tableName}")
    public ModelAndView deleteTable(@PathVariable String tableName,@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
        datasourceService.dropTable(datasource,tableName);
        return showAllTables(id,model);
    }
    
    @RequestMapping("/datasource/showTableDesc/{id}/{name}")
    public ModelAndView showTableDesc(@PathVariable String name,@PathVariable Integer id,Model model) {
    	Datasource datasource = datasourceService.getDatasource(id);
        Map<String, String> map = datasourceService.getTableColumns(datasource,name);
    	model.addAttribute("table", map);
    	model.addAttribute("tableName", name);
    	return new ModelAndView("admin/show-table-desc","model",model);
    }

    @RequestMapping("/datasource/getTableColumn")
    @ResponseBody
    public String showTableColumn(String name,Integer id) {
        Datasource datasource = datasourceService.getDatasource(id);
        Map<String, String> map = datasourceService.getTableColumns(datasource,name);
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
            list_part = datasourceService.getDatasourcesByType("POSTGRESQL");
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
            list_part = datasourceService.getDatasourcesByTypeAndUserId("POSTGRESQL",user.getId());
        }
        model.addAttribute("datalist", list);
        model.addAttribute("postgre", list_part);
        return new ModelAndView("admin/copy-table","model",model);
    }

    @ResponseBody
    @RequestMapping("/datasource/getTableList2")
    public Map<String,Integer> getTableList2(Integer id) {
    	Datasource datasource = datasourceService.getDatasource(id);
        Map<String, Integer> map = datasourceService.getAllTables(datasource);
        return map;
    }
    
    @RequestMapping("/datasource/copyTable")
    public ModelAndView copyTable(Model model,Integer sdatasource,String sname,Integer ddatasource,String dname,HttpSession session) {
    	Datasource datasources = datasourceService.getDatasource(sdatasource);
    	Datasource datasourced = datasourceService.getDatasource(ddatasource);
        datasourceService.copyTable(datasources,sname,datasourced,dname);
        return findAll(model,session);
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
        	if(datasourceService.getDatasourceByNameAndUserId(name,user.getId()) != null){
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
    	List<List<Object>> result = datasourceService.getTableData(datasource,tableName);
    	model.addAttribute("result", result);
    	return new ModelAndView("admin/show-table-data","model",model);
    }
}
