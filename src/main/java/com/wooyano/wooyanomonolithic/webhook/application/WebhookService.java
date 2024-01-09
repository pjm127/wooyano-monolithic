package com.wooyano.wooyanomonolithic.webhook.application;

import com.wooyano.wooyanomonolithic.global.config.redis.RedisServiceWebhook;
import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final RedisServiceWebhook redisServiceWebhook;
    public void saveWebhook(WebhookServiceRequest request) {
        String orderId = request.getData().getOrderId();
        String createdAt = request.getCreatedAt();
        Duration duration = Duration.ofMinutes(15);
        log.info("orderId: {},createdAt: {},duration: {}", orderId, createdAt, duration);
        redisServiceWebhook.setValues(orderId, createdAt, duration);

    }
}
