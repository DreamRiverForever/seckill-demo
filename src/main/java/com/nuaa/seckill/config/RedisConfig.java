package com.nuaa.seckill.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // key的序列化
        template.setKeySerializer(new StringRedisSerializer());
        // value的序列化
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash类型，key序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        // hash类型，value序列化
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 注入连接工厂
        template.setConnectionFactory(factory);
        return template;
    }

//    @Bean
//    public DefaultRedisScript<Boolean> script() {
//        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
//        //lock.lua脚本位置和application.yml同级目录
//        redisScript.setLocation(new ClassPathResource("lock.lua"));
//        redisScript.setResultType(Boolean.class);
//        return redisScript;
//    }
}
