package com.wooyano.wooyanomonolithic.webhook.application;

import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
import com.wooyano.wooyanomonolithic.webhook.domain.ReservationHistoryWebhook;
import com.wooyano.wooyanomonolithic.webhook.infrastructure.ReservationHistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationHistoryWebhookService {
    //private final RedisServiceWebhook redisServiceWebhook;
    private final ReservationHistoryRepository reservationHistoryRepository;

    public void saveWebhook(WebhookServiceRequest request) {
        String orderId = request.getData().getOrderId();
        LocalDateTime createdAt = request.getCreatedAt();
        log.info("orderId: {},createdAt: {}", orderId, createdAt);
        ReservationHistoryWebhook reservationHistory = ReservationHistoryWebhook.create(orderId, createdAt);
        reservationHistoryRepository.save(reservationHistory);

    }
}
