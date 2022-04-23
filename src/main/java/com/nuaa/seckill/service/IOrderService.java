package com.nuaa.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuaa.seckill.pojo.Order;
import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.vo.GoodsVo;
import com.nuaa.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-04-19
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goodsVo);

    OrderDetailVo detail(Long orderId);
}
