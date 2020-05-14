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
    
    @RequestMapping("/joblog/getResult/{id}")
    public ModelAndView getResult(@PathVariable Integer id,Model model){
        try {
            model.addAttribute("jobLog", jobLogService.findLogByJobId(id).getLogResult());
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","日志刷新失败"));
        }
    	return new ModelAndView("admin/job-log","model",model);
    }


}
