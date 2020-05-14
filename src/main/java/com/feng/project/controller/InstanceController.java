package com.feng.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.feng.project.domain.Instance;
import com.feng.project.domain.User;
import com.feng.project.service.InstanceService;
import com.feng.project.service.XxlJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InstanceController {
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private XxlJobService xxlJobService;

    @RequestMapping("/instance/list")
    public ModelAndView findAll(Model model, HttpSession httpSession){
        User user = (User)httpSession.getAttribute("user");
        List<Instance> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")){
           list = instanceService.findAll();
        }else{
           list = instanceService.findAllByUserId(user.getId());
        }
        model.addAttribute("list",list);
        return new ModelAndView("admin/instance-list","model",model);
    }

    @RequestMapping("/instance/content/{id}")
    public ModelAndView getLogContent(@PathVariable Integer id, Model model){
        try{
            model.addAttribute("content",instanceService.getExecLog(id,1));
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","日志解析失败"));
        }
        return new ModelAndView("admin/instance-log","model",model);
    }

    @RequestMapping("/instance/flush")
    public ModelAndView flush(Model model,HttpSession httpSession){
        try {
            List<JSONObject> jobs = xxlJobService.getAllHandleInfo();
            for (JSONObject jsonObject : jobs) {
                instanceService.saveIntoInstance(jsonObject);
            }
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","实例刷新失败"));
        }
        return findAll(model, httpSession);
    }

    @RequestMapping("/instance/flush/{id}")
    public ModelAndView flush(@PathVariable Integer id, Model model,HttpSession httpSession){
        try {
            List<JSONObject> jobs = xxlJobService.getAllHandleInfo();
            for (JSONObject jsonObject : jobs) {
                instanceService.saveIntoInstance(jsonObject);
            }
        }catch (Exception e){
            return new ModelAndView("error","model",model.addAttribute("error","实例刷新失败"));
        }
        User user = (User)httpSession.getAttribute("user");
        List<Instance> list = new ArrayList<>();
        if(user.getRole().equalsIgnoreCase("admin")){
            list = instanceService.findAllByCronjobId(id);
        }else{
            list = instanceService.findAllByUserIdAndCronjobId(user.getId(),id);
        }
        model.addAttribute("list",list);
        model.addAttribute("id",id);
        return new ModelAndView("admin/one-instance-list","model",model);
    }

    @RequestMapping("/instance/listByJobId/{id}")
    public ModelAndView findListByJobId(@PathVariable Integer id,Model model){
        model.addAttribute("list",instanceService.findAllByCronjobId(id));
        model.addAttribute("id",id);
        return new ModelAndView("admin/one-instance-list","model",model);
    }
}
