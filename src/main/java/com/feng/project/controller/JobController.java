package com.feng.project.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.feng.project.domain.Datasource;
import com.feng.project.domain.User;
import com.feng.project.service.JobService;
import com.feng.project.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Job;
import com.feng.project.domain.JobLog;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.JobLogService;
import com.feng.project.util.DataxUtil;
import com.feng.project.util.JobUtil;

import ch.ethz.ssh2.Connection;

@Controller
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private DatasourceService datasourceService;
    @Autowired
    private JobLogService jobLogService;
    
    @RequestMapping("/job/prepare")
    public ModelAndView findAllDatasource(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
        return new ModelAndView("admin/new-job","model",model);
    }

    @PostMapping("/job/saveJob")
    public ModelAndView saveOrUpdate(Job job, HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        if(job.getId()==null) {
            job.setUserId(user.getId());
            job.setReaderDbType(datasourceService.getDatasource(job.getReaderDbId()).getType());
            job.setWriterDbType(datasourceService.getDatasource(job.getWriterDbId()).getType());
            jobService.save(job);
        }else {
            jobService.save(job);
        }
        model.addAttribute("joblist",jobService.findAll());
        return new ModelAndView("admin/job-list","model",model);
    }

    @RequestMapping("/job/jobList")
    public ModelAndView findAllJob(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<Job> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = jobService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = jobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/job-list","model",model);
    }

    @RequestMapping("/job/execJob/{id}")
    public ModelAndView doJobOnce(@PathVariable Integer id,Model model) throws IOException {
        Job job = jobService.getOne(id);
        if(job.getJsonContent() == null) {
            JobUtil.getJsonfile(datasourceService.getDatasource(
                    job.getReaderDbId()), datasourceService.getDatasource(job.getWriterDbId()),
                    job);
        }else{
            JobUtil.getJsonFileByContent(job);
        }
        Connection conn = DataxUtil.login("192.144.129.188", "root", "FFei916#");
        DataxUtil.transferFile(conn, "src/main/resources/static/file/"+job.getName()+".json", "/root/datax/job");
        String result = DataxUtil.execmd(conn, "python /root/datax/bin/datax.py /root/datax/job/"+job.getName()+".json",job.getName());
        JobLog jobLog = new JobLog(id,job.getName(),result,new Date());
        jobLogService.save(jobLog);
        model.addAttribute("jobLog",result);
        return new ModelAndView("admin/job-log","model",model);
    }

    @RequestMapping("/job/addFileJob")
    public ModelAndView addFileJob(Job job, Model model,HttpSession session){
        Map<String,String> map = JsonUtil.testComplexJSONStrToJSONObject(job.getJsonContent());
        job.setReaderDbType(map.get("reader").replace("reader", ""));
        job.setWriterDbType(map.get("writer").replace("writer", ""));
        User user = (User)session.getAttribute("user");
        job.setUserId(user.getId());
        jobService.save(job);
        List<Job> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = jobService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = jobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/job-list","model",model);
    }

    @RequestMapping("/job/delete/{id}")
    public ModelAndView deleteJob(@PathVariable Integer id, Model model,HttpSession session){
        jobService.delete(id);
        User user = (User)session.getAttribute("user");
        List<Job> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = jobService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = jobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/job-list","model",model);
    }

    @ResponseBody
    @RequestMapping("/job/getJobByName")
    public String getJobByName(String name){
        if(jobService.findJobByName(name) != null){
            return "名称已存在";
        }else{
            return "";
        }
    }

    @RequestMapping("/job/search")
    public ModelAndView search(String name,Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<Job> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = jobService.findJobsByName(name);
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = jobService.findJobsByNameAndUserId(name,user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/job-list","model",model);
    }
}
