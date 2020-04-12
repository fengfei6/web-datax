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
import java.io.UnsupportedEncodingException;
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
    public ModelAndView getLogContent(@PathVariable Integer id, Model model) throws UnsupportedEncodingException {
        Instance instance = instanceService.getOne(id);
        model.addAttribute("content",instanceService.getExecLog(id,1));
        return new ModelAndView("admin/instance-log","model",model);
    }

    @RequestMapping("/instance/flush")
    public ModelAndView flush(Model model,HttpSession httpSession) throws UnsupportedEncodingException {
        List<JSONObject> jobs = xxlJobService.getAllHandleInfo();
        for(JSONObject jsonObject : jobs){
            instanceService.saveIntoInstance(jsonObject);
        }
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

    @RequestMapping("/instance/listByTaskId/{taskId}")
    public ModelAndView findListByTaskId(@PathVariable Integer taskId,Model model){
        model.addAttribute("list",instanceService.findAllByTaskId(taskId));
        return new ModelAndView("admin/instance-list","model",model);
    }
}
