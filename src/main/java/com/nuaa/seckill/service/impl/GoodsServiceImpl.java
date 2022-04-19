package com.nuaa.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nuaa.seckill.mapper.GoodsMapper;
import com.nuaa.seckill.pojo.Goods;
import com.nuaa.seckill.service.IGoodsService;
import com.nuaa.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-04-19
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> findGoodsVo() {
        List<GoodsVo> res = goodsMapper.findGoodsVo();
        return res;
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(long goodsId) {
        GoodsVo vo = goodsMapper.findGoodsVoByGoodsId(goodsId);
        return vo;
    }
}
