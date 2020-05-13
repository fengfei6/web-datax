package com.feng.project.controller;


import java.io.IOException;
import java.util.ArrayList;

import java.util.List;



import javax.servlet.http.HttpSession;

import com.feng.project.domain.User;
import com.feng.project.service.JobService;
import com.feng.project.service.XxlJobService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Job;

import com.feng.project.util.DataxUtil;
import com.feng.project.util.JobUtil;

import ch.ethz.ssh2.Connection;

@Controller
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private XxlJobService xxlJobService;

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
    public ModelAndView doJobOnce(@PathVariable Integer id,Model model,HttpSession session){
        try {
            jobService.doJobAndSaveLog(id);
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","任务创建失败"));
        }
        return findAllJob(model, session);
    }

    @RequestMapping("/job/addFileJob")
    public ModelAndView addFileJob(Job job, Model model,HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            job.setUserId(user.getId());
            //上传json到服务器
            JobUtil.getJsonFileByContent(job);
            Connection conn = DataxUtil.login("192.144.129.188", "root", "FFei916#");
            DataxUtil.transferFile(conn, "src/main/resources/static/file/" + job.getName() + "_" + job.getUserId() + ".json", "/root/datax/job");
//            //提交到xxl-job
//            String taskId = xxlJobService.submitJob(job);
//            job.setTaskId(Integer.parseInt(taskId));
            jobService.save(job);
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","任务创建失败"));
        }
        return findAllJob(model, session);
    }

    @RequestMapping("/job/delete/{id}")
    public ModelAndView deleteJob(@PathVariable Integer id, Model model,HttpSession session){
        Job job = jobService.getOne(id);
        xxlJobService.delete(job.getTaskId());
        jobService.delete(id);
        return findAllJob(model, session);
    }

    @ResponseBody
    @RequestMapping("/job/getJobByName")
    public String getJobByName(String name,HttpSession session){
        User user = (User)session.getAttribute("user");
        if(jobService.getJobByNameAndUserId(name,user.getId()) != null){
            return "名称已存在，重新输入";
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
