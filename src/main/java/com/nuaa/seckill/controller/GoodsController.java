package com.nuaa.seckill.controller;


import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/toList")
    public String goods(Model model, User user){
//        if(ticket == null)
//            return "login";
//        // User user = (User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
//        if(null == user)
//            return "login";
        model.addAttribute("user", user);
        return "goodsList";
    }

    @RequestMapping("/toDetail")
    public String toDetail(Model model, User user){

        model.addAttribute("user", user);
        return "goodsList";
    }
}
