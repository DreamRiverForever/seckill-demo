package com.nuaa.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuaa.seckill.pojo.SeckillOrder;
import com.nuaa.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-04-19
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
