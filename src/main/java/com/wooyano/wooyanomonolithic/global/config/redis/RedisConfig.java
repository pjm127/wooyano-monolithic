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

    @Value("${spring.data.redis1.port}")
    private int port1;

    @Value("${spring.data.redis1.host}")
    private String host1;

    @Value("${spring.data.redis2.port}")
    private int port2;
    @Value("${spring.data.redis2.host}")
    private String host2;



    @Bean
    public RedisConnectionFactory redisConnectionFactoryOne() {
        return new LettuceConnectionFactory(host1, port1);
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactoryTwo() {
        return new LettuceConnectionFactory(host2, port2);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplateOne() {
        RedisTemplate<String, String> redisTemplateOne = new RedisTemplate<>();
        redisTemplateOne.setKeySerializer(new StringRedisSerializer());
        redisTemplateOne.setValueSerializer(new StringRedisSerializer());
        redisTemplateOne.setConnectionFactory(redisConnectionFactoryOne());
        return redisTemplateOne;
    }
    @Bean
    public RedisTemplate<String, String> redisTemplateTwo() {
        RedisTemplate<String, String> redisTemplateTwo = new RedisTemplate<>();
        redisTemplateTwo.setKeySerializer(new StringRedisSerializer());
        redisTemplateTwo.setValueSerializer(new StringRedisSerializer());
        redisTemplateTwo.setConnectionFactory(redisConnectionFactoryTwo());
        return redisTemplateTwo;
    }
}