package com.nuaa.seckill.controller;


import com.nuaa.seckill.pojo.Goods;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.IGoodsService;
import com.nuaa.seckill.service.IUserService;
import com.nuaa.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    IUserService userService;
    @Autowired
    IGoodsService goodsService;

    @RequestMapping("/toList")
    public String goods(Model model, User user){
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable long goodsId){
        model.addAttribute("user", user);
        GoodsVo vo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = vo.getStartDate();
        Date endDate = vo.getEndDate();
        Date curDate = new Date();
        // 秒杀状态
        int secKillStatus = 0;
        // 秒杀倒计时
        int remainSeconds = 0;
        if(curDate.before(startDate)){
            secKillStatus = 0;
            remainSeconds = (int) ((startDate.getTime() - curDate.getTime()) / 1000);
        }
        else if(curDate.after(endDate)){
            // 秒杀已结束
            secKillStatus = 2;
            remainSeconds = -1;
        }
        else{
            // 秒杀进行中
            secKillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", vo);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goodsDetail";
    }
}
