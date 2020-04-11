package com.feng.project.controller;

import ch.ethz.ssh2.Connection;
import com.feng.project.domain.CronJob;
import com.feng.project.domain.Datasource;
import com.feng.project.domain.User;
import com.feng.project.service.CronJobService;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.XxlJobService;
import com.feng.project.util.DataxUtil;
import com.feng.project.util.JobUtil;
import com.feng.project.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public ModelAndView save(CronJob cronJob, Model model, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        cronJob.setUserId(user.getId());
        String taskId = xxlJobService.submitCronJob(cronJob);
        cronJob.setTaskId(Integer.parseInt(taskId));
        cronJob.setIsRunning(0);
        cronJobService.save(cronJob);
        JobUtil.getJsonfileForCronJob(datasourceService.getDatasource(cronJob.getReaderDbId()),datasourceService.getDatasource(cronJob.getWriterDbId()),cronJob);
        List<CronJob> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = cronJobService.finAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = cronJobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/prepare")
    public ModelAndView findAllDatasource(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<Datasource> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = datasourceService.findAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = datasourceService.findDatasourcesByUserId(user.getId());
        }
        model.addAttribute("datalist", list);
        return new ModelAndView("admin/new-cronjob","model",model);
    }

    @RequestMapping("/cron/delete/{id}")
    public ModelAndView delete(@PathVariable Integer id, Model model,HttpSession session){
        CronJob cronJob = cronJobService.getOne(id);
        cronJobService.delete(cronJob);
        xxlJobService.delete(cronJob.getTaskId());
        User user = (User) session.getAttribute("user");
        List<CronJob> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = cronJobService.finAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = cronJobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/cronjobList")
    public ModelAndView finAll(Model model,HttpSession session){
        User user = (User)session.getAttribute("user");
        List<CronJob> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = cronJobService.finAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = cronJobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/addFileJob")
    public ModelAndView addFileJob(CronJob cronJob, Model model,HttpSession session){
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
        User user = (User)session.getAttribute("user");
        cronJob.setUserId(user.getId());
        cronJobService.save(cronJob);
        List<CronJob> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = cronJobService.finAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = cronJobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/status/{id}")
    public ModelAndView changeStatus(@PathVariable Integer id,Model model,HttpSession session) throws IOException {
        CronJob cronJob = cronJobService.getOne(id);
        if(cronJob.getIsRunning() == 0){
            Connection conn = DataxUtil.login("192.144.129.188", "root", "FFei916#");
            DataxUtil.transferFile(conn, "src/main/resources/static/file/"+cronJob.getName()+".json", "/root/datax/job");
            xxlJobService.onScheduling(cronJob);
            cronJob.setIsRunning(1);
            cronJobService.update(cronJob);
        }else{
            xxlJobService.cancelScheduling(cronJob);
            cronJob.setIsRunning(0);
            cronJobService.update(cronJob);
        }
        User user = (User) session.getAttribute("user");
        List<CronJob> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = cronJobService.finAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = cronJobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
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

    @RequestMapping("/cron/search")
    public ModelAndView search(String name,String isRunning,Model model,HttpSession session){
        User user = (User) session.getAttribute("user");
        List<CronJob> list = new ArrayList<>();
        if(isRunning.equalsIgnoreCase("all")){
            if(user.getRole().equalsIgnoreCase("admin")) {
               list = cronJobService.findJobsByName(name);
            }else if(user.getRole().equalsIgnoreCase("user")){
                list = cronJobService.findJobsByNameAndUserId(name,user.getId());
            }
        }else {
            if(user.getRole().equalsIgnoreCase("admin")) {
                list = cronJobService.findJobsByNameAndIsRunning(name,Integer.parseInt(isRunning));
            }else if(user.getRole().equalsIgnoreCase("user")){
                list = cronJobService.findJobsByNameAndIsRunningAndUserId(name,Integer.parseInt(isRunning),user.getId());
            }
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/trigger/{id}")
    public ModelAndView trigger(@PathVariable Integer id,Model model,HttpSession session){
        CronJob cronJob = cronJobService.getOne(id);
        xxlJobService.executeUnder(cronJob.getTaskId(),"");
        User user = (User) session.getAttribute("user");
        List<CronJob> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")) {
            list = cronJobService.finAll();
        }else if(user.getRole().equalsIgnoreCase("user")){
            list = cronJobService.findJobsByUserId(user.getId());
        }
        model.addAttribute("joblist",list);
        return new ModelAndView("admin/cronjob-list","model",model);
    }

    @RequestMapping("/cron/log/{id}")
    public void log(@PathVariable Integer id){
        CronJob cronJob = cronJobService.getOne(id);
        System.out.println(xxlJobService.getHandleInfo(cronJob.getTaskId()));
    }
}
