package com.yeb.centre.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author dai
 * @create 2022-01-2022/1/25  16-31-56
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        //设置String类型的key属性
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置String类型的value属性
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //设置hash类型的key属性
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //设置hash类型的value属性
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        //设置连接器
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
