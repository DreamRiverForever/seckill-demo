package com.nuaa.seckill.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nuaa.seckill.pojo.Order;
import com.nuaa.seckill.pojo.SeckillMessage;
import com.nuaa.seckill.pojo.SeckillOrder;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.rabbitmq.MQSender;
import com.nuaa.seckill.service.IGoodsService;
import com.nuaa.seckill.service.IOrderService;
import com.nuaa.seckill.service.ISeckillGoodsService;
import com.nuaa.seckill.service.ISeckillOrderService;
import com.nuaa.seckill.utils.JsonUtil;
import com.nuaa.seckill.vo.GoodsVo;
import com.nuaa.seckill.vo.RespBean;
import com.nuaa.seckill.vo.RespBeanEnum;
import lombok.val;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    Map<Long, Boolean> emptyStockMap = new HashMap<>();

    @RequestMapping("/doSecKill2")
    public String doSecKill2(Model model, User user, Long goodsId) {
        if (user == null)
            return "login";
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBean.error(RespBeanEnum.EMPTY_STOCK));
            return "secKillFail";
        }

        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
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
    public RespBean doSecKill(User user, Long goodsId) {
        if (user == null)
            return RespBean.error(RespBeanEnum.SESSION_ERROR);

        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPETITION_ERROR);
        }
        boolean check = emptyStockMap.get(goodsId);
        if (check) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();

        // redis中减库存
        long stock = valueOperations.decrement("seckillGoods:" + goodsId);

        // 如果库存小于0了，表示没有商品了，就不能秒杀
        if (stock < 0) {
            emptyStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 定义一个消息
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        // 发送这个消息给消费者
        mqSender.sendSecKillMessage(JsonUtil.object2JsonStr(seckillMessage));

        // 返回状态码200，并且对象0表示正在发送
        return RespBean.success(0);

        /*
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

         */
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }





    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVos)) {
            return;
        }
        goodsVos.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);
        });

    }
}
