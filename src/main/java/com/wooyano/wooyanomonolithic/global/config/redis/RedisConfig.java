package com.wooyano.wooyanomonolithic.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port1;

    @Value("${spring.data.redis.host}")
    private String host1;



    @Bean
    public RedisConnectionFactory redisConnectionFactoryOne() {
        return new LettuceConnectionFactory(host1, port1);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplateOne = new RedisTemplate<>();
        redisTemplateOne.setKeySerializer(new StringRedisSerializer());
        redisTemplateOne.setValueSerializer(new StringRedisSerializer());
        redisTemplateOne.setConnectionFactory(redisConnectionFactoryOne());
        return redisTemplateOne;
    }

}