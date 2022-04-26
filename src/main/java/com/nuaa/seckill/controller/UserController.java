package com.nuaa.seckill.controller;


import com.nuaa.seckill.pojo.User;
import com.nuaa.seckill.rabbitmq.MQSender;
import com.nuaa.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2022-04-14
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

    @RequestMapping("/mq")
    @ResponseBody
    public void mq() {
        mqSender.send("你好兔子");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq01() {
        mqSender.send("你好兔子，交换机");
    }

    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mq02() {
        mqSender.send01("你好red");
    }

    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq03() {
        mqSender.send02("你好green");
    }


    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mq04() {
        mqSender.send01("你好red");
    }

    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mq05() {
        mqSender.send02("你好green");
    }

}
