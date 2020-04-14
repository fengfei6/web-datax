package com.feng.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.feng.project.domain.JobLog;
import com.feng.project.service.InstanceService;
import com.feng.project.service.JobLogService;
import com.feng.project.service.XxlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class JobLogController {
    @Autowired
    private JobLogService jobLogService;
    @Autowired
    private XxlJobService xxlJobService;
    
    @RequestMapping("/joblog/joblogs/{jobId}")
    public ModelAndView findAll(@PathVariable Integer jobId,Model model)throws UnsupportedEncodingException {
        List<JSONObject> jobs = xxlJobService.getAllHandleInfo();
        for(JSONObject jsonObject : jobs){
            jobLogService.saveIntoLog(jsonObject);
        }
    	model.addAttribute("joblogs",jobLogService.findByJobId(jobId));
        model.addAttribute("jobId",jobId);
    	return new ModelAndView("admin/joblog-list","model",model);
    }
    
    @RequestMapping("/joblog/getResult/{id}")
    public ModelAndView getResult(@PathVariable Integer id,Model model)throws UnsupportedEncodingException {
    	model.addAttribute("jobLog",jobLogService.getExecLog(id,1));
    	return new ModelAndView("admin/job-log","model",model);
    }


}
