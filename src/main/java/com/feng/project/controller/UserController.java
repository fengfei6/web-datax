package com.feng.project.controller;

import com.feng.project.domain.User;
import com.feng.project.service.MailService;
import com.feng.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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
}
