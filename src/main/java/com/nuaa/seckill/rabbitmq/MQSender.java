package com.nuaa.seckill.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Fanout模式
    public void send(Object obj) {
        log.info("发消息:" + obj);
        rabbitTemplate.convertAndSend("fanoutExchange", "", obj);
    }

    public void send01(Object obj) {
        log.info("发red消息:" + obj);
        rabbitTemplate.convertAndSend("directExchange", "queue.red", obj);
    }

    // direct模式
    public void send02(Object obj) {
        log.info("发green消息:" + obj);
        rabbitTemplate.convertAndSend("directExchange", "queue.green", obj);
    }

    // topic模式
    public void send03(Object msg) {
        log.info("发送消息(QUEUE01接收)：" + msg);
        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
    }

    public void send04(Object msg) {
        log.info("发送消息(QUEUE02接收)：" + msg);
        rabbitTemplate.convertAndSend("topicExchange", "green.queue.green.message", msg);
    }


}
