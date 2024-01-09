package com.wooyano.wooyanomonolithic.global.config.redis;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RedisServiceWebhook {
    @Autowired
    @Qualifier("redisTemplateTwo")
    private RedisTemplate<String, String> redisTemplateTwo;

    public void setValues(String key, String value, Duration duration){
        redisTemplateTwo.opsForValue().set(key, value, duration);
    }
    @Transactional(readOnly = true)
    public String getValues(String key){
        return redisTemplateTwo.opsForValue().get(key);
    }
}
