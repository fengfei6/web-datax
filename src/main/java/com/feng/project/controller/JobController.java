package com.feng.project.controller;

import com.feng.project.domain.Job;
import com.feng.project.domain.User;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.JobService;
import com.feng.project.util.DataxUtil;
import com.feng.project.util.JobUtil;

import ch.ethz.ssh2.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private DatasourceService datasourceService;

    @RequestMapping("/job/prepare")
    public ModelAndView findAllDatasource(Model model){
        model.addAttribute("datalist",datasourceService.findAll());
        return new ModelAndView("admin/new-job","model",model);
    }

    @PostMapping("/job/saveJob")
    public ModelAndView saveOrUpdate(Job job, HttpServletRequest request, Model model){
        //User user = (User)request.getSession().getAttribute("user");
        if(job.getId()==null) {
            //job.setUserId(user.getId());
            jobService.save(job);
        }else {
            jobService.save(job);
        }
        model.addAttribute("joblist",jobService.findAll());
        return new ModelAndView("admin/job-list","model",model);
    }

    @RequestMapping("/job/jobList")
    public ModelAndView findAllJob(Model model){
        model.addAttribute("joblist",jobService.findAll());
        return new ModelAndView("admin/job-list","model",model);
    }

    @RequestMapping("/job/execJob/{id}")
    public ModelAndView doJobOnce(@PathVariable Integer id,Model model) throws IOException {
        Job job = jobService.getOne(id);
        JobUtil.getJsonfile(datasourceService.getDatasource(
                job.getReaderDbId()),datasourceService.getDatasource(job.getWriterDbId()),
                job.getReaderTable(),job.getWriterTable(),job.getName());
        Connection conn = DataxUtil.login("192.144.129.188", "root", "FFei916#");
        DataxUtil.transferFile(conn, "src/main/resources/static/file/job.json", "/usr/local/datax/job");
        String result = DataxUtil.execmd(conn, "python /usr/local/datax/bin/datax.py /usr/local/datax/job/job.json",job.getName());
        return getJobLog(model, job.getName());
    }
    
    public ModelAndView getJobLog(Model model,String name) throws IOException{
    	FileReader fr = new FileReader("src/main/resources/static/log/"+name+".log"); 
    	StringBuilder sb = new StringBuilder();
    	int ch = 0;  
    	while((ch = fr.read())!=-1 ){   
    		sb.append((char)ch);   
    	} 
        model.addAttribute("jobLog",sb.toString());
        return new ModelAndView("admin/job-log","model",model);
    }
}
