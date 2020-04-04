package com.feng.project.controller;

import com.feng.project.domain.CronJob;
import com.feng.project.domain.Datasource;
import com.feng.project.service.CronJobService;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.XxlJobService;
import com.feng.project.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class CronJobController {
    @Autowired
    private CronJobService cronJobService;
    @Autowired
    private XxlJobService xxlJobService;

    @Autowired
    private DatasourceService datasourceService;
    @RequestMapping("/cron/add")
    public ModelAndView save(CronJob cronJob, Model model){
        String taskId = xxlJobService.submitCronJob(cronJob);
        cronJob.setTaskId(Integer.parseInt(taskId));
        cronJob.setIsRunning(0);
        cronJobService.save(cronJob);
        model.addAttribute("joblist",cronJobService.finAll());
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/prepare")
    public ModelAndView findAllDatasource(Model model){
        model.addAttribute("datalist",datasourceService.findAll());
        return new ModelAndView("admin/new-cronjob","model",model);
    }

    @RequestMapping("/cron/delete/{id}")
    public ModelAndView delete(@PathVariable Integer id, Model model){
        CronJob cronJob = cronJobService.getOne(id);
        cronJobService.delete(cronJob);
        xxlJobService.delete(cronJob.getTaskId());
        model.addAttribute("joblist",cronJobService.finAll());
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/cronjobList")
    public ModelAndView finAll(Model model){
        model.addAttribute("joblist",cronJobService.finAll());
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/addFileJob")
    public ModelAndView addFileJob(CronJob cronJob, Model model){
        Map<String,String> map = JsonUtil.testComplexJSONStrToJSONObject(cronJob.getJsonContent());
        cronJob.setReaderTable(map.get("tabler"));
        cronJob.setWriterTable(map.get("tablew"));
        String[] params = JsonUtil.getParamsArray(map.get("reader"));
        Datasource datasource = datasourceService.findDatasourceByDbnameAndIpAndPort(params[0],params[1],params[2]);
        cronJob.setReaderDbId(datasource.getId());
        params = JsonUtil.getParamsArray(map.get("writer"));
        datasource = datasourceService.findDatasourceByDbnameAndIpAndPort(params[0],params[1],params[2]);
        cronJob.setWriterDbId(datasource.getId());
        String taskId = xxlJobService.submitCronJob(cronJob);
        cronJob.setTaskId(Integer.parseInt(taskId));
        cronJob.setIsRunning(0);
        cronJobService.save(cronJob);
        model.addAttribute("joblist",cronJobService.finAll());
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/status/{id}")
    public ModelAndView changeStatus(@PathVariable Integer id,Model model){
        CronJob cronJob = cronJobService.getOne(id);
        if(cronJob.getIsRunning() == 0){
            xxlJobService.onScheduling(cronJob);
            cronJob.setIsRunning(1);
            cronJobService.update(cronJob);
        }else{
            xxlJobService.cancelScheduling(cronJob);
            cronJob.setIsRunning(0);
            cronJobService.update(cronJob);
        }
        model.addAttribute("joblist",cronJobService.finAll());
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @ResponseBody
    @RequestMapping("/cron/getJobByName")
    public String getJobByName(String name){
        if(cronJobService.findJobByName(name) != null){
            return "名称已存在";
        }else{
            return "";
        }
    }
}
