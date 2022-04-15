package com.nuaa.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor

public enum RespBeanEnum {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务器异常"),

    LOGIN_ERROR(500210, "用户名密码错误"),
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    BIND_ERROR(500212, "参数校验异常");
    private final Integer code;
    private final String message;

}
