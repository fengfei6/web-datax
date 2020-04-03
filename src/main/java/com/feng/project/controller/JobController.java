package com.feng.project.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feng.project.domain.Datasource;
import com.feng.project.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.feng.project.domain.Job;
import com.feng.project.domain.JobLog;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.JobLogService;
import com.feng.project.service.JobService;
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
    
    @RequestMapping("/job/addFileJob")
    public ModelAndView addFileJob(Job job, Model model){
        Map<String,String> map = JsonUtil.testComplexJSONStrToJSONObject(job.getJsonContent());
        job.setReaderTable(map.get("tabler"));
        job.setWriterTable(map.get("tablew"));
        String[] params = JsonUtil.getParamsArray(map.get("reader"));
        Datasource datasource = datasourceService.findDatasourceByDbnameAndIpAndPort(params[0],params[1],params[2]);
        job.setReaderDbId(datasource.getId());
        params = JsonUtil.getParamsArray(map.get("writer"));
        datasource = datasourceService.findDatasourceByDbnameAndIpAndPort(params[0],params[1],params[2]);
        job.setWriterDbId(datasource.getId());
        jobService.save(job);
        model.addAttribute("joblist",jobService.findAll());
        return new ModelAndView("admin/job-list","model",model);
    }

    @RequestMapping("/job/delete/{id}")
    public ModelAndView deleteJob(@PathVariable Integer id, Model model){
        jobService.delete(id);
        model.addAttribute("joblist",jobService.findAll());
        return new ModelAndView("admin/job-list","model",model);
    }
}
