package com.feng.project.controller;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Datasource;
import com.feng.project.domain.User;
import com.feng.project.service.DatasourceService;
import com.feng.project.util.ConnectionUtil;

@Controller
public class DatasourceController {
    @Autowired
    private DatasourceService databaseService;

    @PostMapping("/datasource/add")
    public ModelAndView saveDB(Datasource datasource, HttpServletRequest request,Model model){
        User user = (User)request.getSession().getAttribute("user");
        if(datasource.getId()==null) {
	        datasource.setUserId(user.getId());
	        datasource.setIsConnection("0");
	        databaseService.save(datasource);
        }else {
        	databaseService.update(datasource);
        }
        model.addAttribute("datalist",databaseService.findAll());
        return new ModelAndView("admin/database-list","model",model);
    }

    @RequestMapping("/datasource/list")
    public ModelAndView findAll(Model model){
    	model.addAttribute("datalist",databaseService.findAll());
        return new ModelAndView("admin/database-list","model",model);
    }

    @RequestMapping("/datasource/testConn/{id}")
    public ModelAndView testConn(HttpServletRequest request, HttpServletResponse response, @PathVariable String id,Model model){
        Datasource datasource = databaseService.getDatasource(Integer.parseInt(id));
        boolean flag = ConnectionUtil.isConn(datasource,"mysql");
        if(flag) {
        	datasource.setIsConnection("1");
        }else {
        	datasource.setIsConnection("0");
        }
        databaseService.update(datasource);
        model.addAttribute("datalist", databaseService.findAll());
        model.addAttribute("flag", flag);
        return new ModelAndView("admin/database-list","model",model);
    }
    
    @RequestMapping("/datasource/edit/{id}")
    public ModelAndView getOneDatesource(@PathVariable Integer id,Model model) {
    	Datasource datasource = databaseService.getDatasource(id);
    	return new ModelAndView("admin/edit-database","datasource",datasource);
    }
    
    @RequestMapping("/datasource/delete/{id}")
    public ModelAndView deleteDatesource(@PathVariable Integer id,Model model) {
    	databaseService.delete(id);
    	model.addAttribute("datalist", databaseService.findAll());
    	return new ModelAndView("admin/database-list","model",model);
    }
    
    @RequestMapping("/datasource/showAll/{id}")
    public ModelAndView showAllTables(@PathVariable Integer id,Model model) {
    	Datasource datasource = databaseService.getDatasource(id);
    	Connection conn = ConnectionUtil.getConn(datasource, datasource.getType());
    	model.addAttribute("id",id);
    	model.addAttribute("tablelist",ConnectionUtil.getTables(ConnectionUtil.getMetaDate(conn)));
    	return new ModelAndView("admin/show-table","model",model);
    }
    
    @RequestMapping("/datasource/showTableDesc/{id}/{name}")
    public ModelAndView showTableDesc(@PathVariable String name,@PathVariable Integer id,Model model) {
    	Datasource datasource = databaseService.getDatasource(id);
    	Connection conn = ConnectionUtil.getConn(datasource, datasource.getType());
    	model.addAttribute("table",ConnectionUtil.getColumn(ConnectionUtil.getMetaDate(conn), name));
    	model.addAttribute("tableName", name);
    	model.addAttribute("primaryKeys",ConnectionUtil.getPrimaryKey(ConnectionUtil.getMetaDate(conn), name));
    	return new ModelAndView("admin/show-table-desc","model",model);
    }
    
}
