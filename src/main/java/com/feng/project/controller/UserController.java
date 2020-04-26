package com.feng.project.controller;

import com.feng.project.domain.User;
import com.feng.project.service.MailService;
import com.feng.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @PostMapping("/user/login")
    public ModelAndView login(String name, String password, Model model, HttpSession session){
        User user = userService.findUserByNameAndPass(name,password);
        if(user == null){
            model.addAttribute("msg","error");
            return new ModelAndView("login","error",model);
        }
        session.setAttribute("user",user);
        model.addAttribute("name",user.getName());
        return new ModelAndView("index","model",model);
    }

    @PostMapping("/user/register")
    public ModelAndView register(User user){
        user.setRole("user");
        userService.save(user);
        return new ModelAndView("login");
    }

    @PostMapping("/user/forgot_pass")
    public ModelAndView forgot(String email){
        String subject = "重置密码";
        int pass = (int)((Math.random()*9+1)*100000);
        userService.updateUserPassByEmail(email,Integer.toString(pass));
        mailService.sendSimpleEmail(email,subject,Integer.toString(pass));
        return new ModelAndView("login");
     }
    
    @RequestMapping("/user/findAll")
    public ModelAndView findAll(Model model) {
    	model.addAttribute("userlist", userService.findAll());
    	return new ModelAndView("admin/user-list","model",model);
    }

    @PostMapping("/user/add")
    public ModelAndView addUser(User user,Model model){
        userService.save(user);
        model.addAttribute("userlist",userService.findAll());
        return new ModelAndView("admin/user-list","model",model);
    }

    
    @RequestMapping("/user/edit/{id}")
    public ModelAndView updateUser(@PathVariable Integer id,Model model){
        model.addAttribute("user",userService.getOne(id));
        return new ModelAndView("admin/edit-user","model",model);
    }
    
    @RequestMapping("/user/delete/{id}")
    public ModelAndView deleteUser(@PathVariable Integer id,Model model){
    	userService.delete(id);
        model.addAttribute("userlist",userService.findAll());
        return new ModelAndView("admin/user-list","model",model);
    }
    
    @RequestMapping("/user/search")
    public ModelAndView searchUsersByName(String name,Model model){
        model.addAttribute("userlist",userService.searchUsersByName(name));
        return new ModelAndView("admin/user-list","model",model);
    }

    @ResponseBody
    @RequestMapping("/user/getUserByName")
    public String getUserByName(String name){
        if(userService.findUserByName(name) != null){
            return "用户名已存在";
        }else{
            return "";
        }
    }
}
