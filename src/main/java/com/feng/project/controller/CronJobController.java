package com.feng.project.controller;

import ch.ethz.ssh2.Connection;
import com.feng.project.domain.CronJob;
import com.feng.project.domain.Datasource;
import com.feng.project.domain.User;
import com.feng.project.service.CronJobService;
import com.feng.project.service.DatasourceService;
import com.feng.project.service.InstanceService;
import com.feng.project.service.XxlJobService;
import com.feng.project.util.DataxUtil;
import com.feng.project.util.JobUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CronJobController {
    @Autowired
    private CronJobService cronJobService;
    @Autowired
    private XxlJobService xxlJobService;
    @Autowired
    private InstanceService instanceService;

    @Autowired
    private DatasourceService datasourceService;

    @Value("${datax.ip}")
    private String ip;

    @Value("${datax.username}")
    private String username;

    @Value("${datax.password}")
    private String password;

    @Value("${datax.job.path}")
    private String path;

    @RequestMapping("/cron/add")
    public ModelAndView save(CronJob cronJob, Model model, HttpSession session){
        try {
            User user = (User) session.getAttribute("user");
            cronJob.setUserId(user.getId());
            String taskId = xxlJobService.submitCronJob(cronJob);
            cronJob.setTaskId(Integer.parseInt(taskId));
            cronJobService.save(cronJob);
            JobUtil.getJsonfileForCronJob(datasourceService.getDatasource(cronJob.getReaderDbId()), datasourceService.getDatasource(cronJob.getWriterDbId()), cronJob);
            Connection conn = DataxUtil.login(ip, username, password);
            DataxUtil.transferFile(conn, "src/main/resources/static/file/" + cronJob.getName() + "_" + cronJob.getUserId() + ".json", path);
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","任务创建失败"));
        }
        return findAll(model, session);
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
        xxlJobService.delete(cronJob.getTaskId());
        instanceService.deleteAllByCronjobId(id);
        cronJobService.delete(cronJob);
       return findAll(model,session);
    }

    @RequestMapping("/cron/cronjobList")
    public ModelAndView findAll(Model model,HttpSession session){
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

    @RequestMapping("/cron/status/{id}")
    public ModelAndView changeStatus(@PathVariable Integer id,Model model,HttpSession session){
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
        return findAll(model, session);
    }

    @ResponseBody
    @RequestMapping("/cron/getJobByName")
    public String getJobByName(String name,HttpSession session){
        User user = (User)session.getAttribute("user");
        if(cronJobService.getJobByNameAndUserId(name,user.getId()) != null){
            return "名称已存在，重新输入";
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
        return findAll(model, session);
    }

}
