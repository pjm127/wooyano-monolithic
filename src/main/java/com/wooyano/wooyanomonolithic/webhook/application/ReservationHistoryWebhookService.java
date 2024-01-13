package com.wooyano.wooyanomonolithic.webhook.application;

import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
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
    private final ReservationHistoryRepository reservationHistoryRepository;

    public void saveWebhook(WebhookServiceRequest request) {
        String orderId = request.getData().getOrderId();
        String status = request.getData().getStatus();
        PaymentStatus paymentStatus = PaymentStatus.findByValue(status);
        LocalDateTime createdAt = request.getCreatedAt();
        log.info("orderId: {},createdAt: {},status={}", orderId, createdAt,status);
        ReservationHistoryWebhook reservationHistory = ReservationHistoryWebhook.create(orderId, createdAt,paymentStatus);
        reservationHistoryRepository.save(reservationHistory);
    }

    public long countByApprovedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return reservationHistoryRepository.countByApprovedAtBetween(startDateTime, endDateTime,PaymentStatus.DONE);
    }
}
