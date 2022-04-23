package com.nuaa.seckill.vo;


import com.nuaa.seckill.pojo.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {

    private Order order;
    private GoodsVo goodsVo;
}
