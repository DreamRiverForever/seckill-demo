package com.nuaa.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nuaa.seckill.pojo.Goods;
import com.nuaa.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2022-04-19
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(long goodsId);
}
