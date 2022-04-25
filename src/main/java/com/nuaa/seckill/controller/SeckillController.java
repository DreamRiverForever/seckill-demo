package com.nuaa.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nuaa.seckill.pojo.Order;
import com.nuaa.seckill.pojo.SeckillOrder;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.IGoodsService;
import com.nuaa.seckill.service.IOrderService;
import com.nuaa.seckill.service.ISeckillGoodsService;
import com.nuaa.seckill.service.ISeckillOrderService;
import com.nuaa.seckill.vo.GoodsVo;
import com.nuaa.seckill.vo.RespBean;
import com.nuaa.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;
    
    @RequestMapping("/doSecKill2")
    public String doSecKill2(Model model, User user, Long goodsId){
        if (user == null)
            return "login";
        model.addAttribute("user" , user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount() < 1){
            model.addAttribute("errmsg", RespBean.error(RespBeanEnum.EMPTY_STOCK));
            return "secKillFail";
        }

        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null){
            model.addAttribute("errmsg", RespBean.error(RespBeanEnum.REPETITION_ERROR));
            return "secKillFail";
        }

        Order order = orderService.seckill(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }


    @RequestMapping(value = "/doSecKill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(User user, Long goodsId){
        if (user == null)
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if(goodsVo.getStockCount() < 1){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null){
            return RespBean.error(RespBeanEnum.REPETITION_ERROR);
        }

        Order order = orderService.seckill(user, goodsVo);

        return RespBean.success(order);
    }
}
