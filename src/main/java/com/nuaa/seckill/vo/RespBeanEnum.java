package com.nuaa.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor

public enum RespBeanEnum {
    // 通用模块
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务器异常"),

    // 登录模块
    LOGIN_ERROR(500210, "用户名密码错误"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BIND_ERROR(500212, "参数校验异常"),
    // 秒杀模块5005xx
    EMPTY_STOCK(500500, "库存不足"),
    REPETITION_ERROR(500501, "该商品每人限购一件");



    private final Integer code;
    private final String message;

}
