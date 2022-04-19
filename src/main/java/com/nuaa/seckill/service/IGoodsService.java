package com.nuaa.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nuaa.seckill.pojo.Goods;
import com.nuaa.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-04-19
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(long goodsId);
}
