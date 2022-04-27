package com.nuaa.seckill.controller;

import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.IGoodsService;
import com.nuaa.seckill.service.IUserService;
import com.nuaa.seckill.vo.DetailVo;
import com.nuaa.seckill.vo.GoodsVo;
import com.nuaa.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    IUserService userService;
    @Autowired
    IGoodsService goodsService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String goods(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        // Redis获取页面，不为空直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        // 如果为空，手动渲染，存入redis缓存
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale()
        , model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable long goodsId, HttpServletRequest request, HttpServletResponse response){
        // Redis取页面，取到了直接返回
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)){
            return html;
        }
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
        // 如果为空，手动渲染，存入redis缓存
        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if (!StringUtils.isEmpty(html)){
            valueOperations.set("goodsDetail:" + goodsId, html, 60, TimeUnit.SECONDS);
        }
        return html;
    }


    @RequestMapping("/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable long goodsId){
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
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(vo);
        detailVo.setSecKillStatus(secKillStatus);
        detailVo.setRemainSeconds(remainSeconds);
//        System.out.println(detailVo);

        return RespBean.success(detailVo);
    }
}
