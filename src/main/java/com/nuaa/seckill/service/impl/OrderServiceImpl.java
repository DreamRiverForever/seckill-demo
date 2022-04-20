package com.nuaa.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuaa.seckill.mapper.OrderMapper;
import com.nuaa.seckill.mapper.SeckillOrderMapper;
import com.nuaa.seckill.pojo.Order;
import com.nuaa.seckill.pojo.SeckillGoods;
import com.nuaa.seckill.pojo.SeckillOrder;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.service.IOrderService;
import com.nuaa.seckill.service.ISeckillGoodsService;
import com.nuaa.seckill.service.ISeckillOrderService;
import com.nuaa.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-04-19
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Override
    public Order seckill(User user, GoodsVo goods) {

        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        seckillGoodsService.updateById(seckillGoods);
        // 生成订单
        Order order = new Order();
        order.setGoodsId(goods.getId());
        order.setUserId(user.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateData(new Date());

        orderMapper.insert(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        // 插入order订单后会自动返回id
        seckillOrder.setOrderId(order.getId());

        seckillOrderService.save(seckillOrder);
        return order;
    }
}
