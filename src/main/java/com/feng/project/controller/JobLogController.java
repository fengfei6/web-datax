package com.feng.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.feng.project.service.JobLogService;
import com.feng.project.service.JobService;
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
    private JobService jobService;
    
//    @RequestMapping("/joblog/joblogs/{jobId}")
//    public ModelAndView findAll(@PathVariable Integer jobId,Model model){
//        try{
//            List<JSONObject> jobs = xxlJobService.getAllHandleInfo();
//            for(JSONObject jsonObject : jobs){
//                jobLogService.saveIntoLog(jsonObject);
//            }
//        }catch (Exception e){
//            return new ModelAndView("error","model",model.addAttribute("error","日志刷新失败"));
//        }
//    	model.addAttribute("joblogs",jobLogService.findByJobId(jobId));
//        model.addAttribute("jobId",jobId);
//    	return new ModelAndView("admin/joblog-list","model",model);
//    }
    
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
