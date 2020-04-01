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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @PostMapping("/user/login")
    public ModelAndView login(String name, String password, Model model, HttpServletRequest request){
        User user = userService.findUserByNameAndPass(name,password);
        if(user == null){
            model.addAttribute("msg","error");
            return new ModelAndView("login","error",model);
        }
        request.getSession().setAttribute("user",user);
        model.addAttribute("name",user.getName());
        return new ModelAndView("index","user",model);
    }

    @PostMapping("/user/register")
    public ModelAndView register(User user){
        user.setRole("user");
        userService.save(user);
        return new ModelAndView("register");
    }

    @PostMapping("/user/forgot_pass")
    public ModelAndView forgot(String email){
        String subject = "重置密码";
        int pass = (int)((Math.random()*9+1)*100000);
        userService.updateUserPassByEmail(email,Integer.toString(pass));
        mailService.sendSimpleEmail(email,subject,Integer.toString(pass));
        return new ModelAndView("index");
     }
    
    @RequestMapping("/user/findAll")
    public ModelAndView findAll(Model model) {
    	model.addAttribute("userlist", userService.findAll());
    	return new ModelAndView("admin/user-list","model",model);
    }
    
    @RequestMapping("/user/modifyRole/{id}")
    public ModelAndView modifyRole(@PathVariable Integer id,Model model) throws InterruptedException {
    	User user = userService.getOne(id);
    	String role = "admin";
    	if("admin".equals(user.getRole())) {
    		role = "user";
    	}
    	userService.modifyRole(id, role);
    	return findAll(model);
    }

    @PostMapping("/user/add")
    public ModelAndView addUser(User user,Model model){
        userService.save(user);
        model.addAttribute("userlist",userService.findAll());
        return new ModelAndView("admin/user-list","model",model);
    }
    
    @RequestMapping("/user/modifyPass/{id}/{password}")
    public ModelAndView modifyPass(@PathVariable Integer id,@PathVariable String password,Model model) throws InterruptedException {
    	userService.modifyPass(id, password);
    	return findAll(model);
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
}
