package com.feng.project.controller;

import com.feng.project.service.JobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JobLogController {
    @Autowired
    private JobLogService jobLogService;
    
    @RequestMapping("/joblog/joblogs/{jobId}")
    public ModelAndView findAll(@PathVariable Integer jobId,Model model) {
    	model.addAttribute("joblogs",jobLogService.findByJobId(jobId));
    	return new ModelAndView("admin/joblog-list","model",model);
    }
    
    @RequestMapping("/joblog/getResult/{id}")
    public ModelAndView getResult(@PathVariable Integer id,Model model) {
    	model.addAttribute("jobLog", jobLogService.getOne(id).getLogResult());
    	return new ModelAndView("admin/job-log","model",model);
    }
    
    @RequestMapping("/joblog/delete/{jobId}/{id}")
    public ModelAndView deleteJobLog(@PathVariable Integer jobId,@PathVariable Integer id,Model model) {
    	jobLogService.delete(id);
    	model.addAttribute("joblogs",jobLogService.findByJobId(jobId));
    	return new ModelAndView("admin/job-log","model",model);
    }
}
