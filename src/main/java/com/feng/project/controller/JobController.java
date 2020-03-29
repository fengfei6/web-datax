package com.feng.project.controller;

import com.feng.project.domain.Job;
import com.feng.project.domain.User;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.JobService;
import com.feng.project.util.JobUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @RequestMapping("/job/execJob")
    public List<String> doJobOnce(Integer id) throws IOException {
        Job job = jobService.getOne(id);
        JobUtil.getJsonfile(datasourceService.getDatasource(
                job.getReaderDbId()),datasourceService.getDatasource(job.getWriterDbId()),
                job.getReaderTable(),job.getWriterTable(),job.getName());
        List<String> list = new ArrayList<>();
        list.add("succ");
        return list;
    }
}
