package com.wooyano.wooyanomonolithic.global.config.redis;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void setValues(String key, String value, Duration duration){
        redisTemplate.opsForValue().set(key, value, duration);
    }
    @Transactional(readOnly = true)
    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }
}
