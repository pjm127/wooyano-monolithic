package com.wooyano.wooyanomonolithic.webhook.application;

import static com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus.DONE;

import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
import com.wooyano.wooyanomonolithic.webhook.domain.ReservationHistoryWebhook;
import com.wooyano.wooyanomonolithic.webhook.infrastructure.ReservationHistoryWebhookRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationHistoryWebhookService {
    private final ReservationHistoryWebhookRepository reservationHistoryRepository;
    private final PaymentRepository paymentRepository;

    public void saveWebhook(WebhookServiceRequest request) {
        String orderId = request.getData().getOrderId();
        String status = request.getData().getStatus();
        PaymentStatus paymentStatus = PaymentStatus.findByValue(status);
        LocalDateTime createdAt = request.getCreatedAt();
        log.info("orderId: {},createdAt: {},status={}", orderId, createdAt,status);
        ReservationHistoryWebhook reservationHistory = ReservationHistoryWebhook.create(orderId, createdAt,paymentStatus);
        reservationHistoryRepository.save(reservationHistory);
    }

    public boolean comparePaymentAndWebhookCount(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        long countWebhook = reservationHistoryRepository.countByApprovedAtBetweenAndStatus(startDateTime, endDateTime, DONE);
        long countPayment = paymentRepository.countByApprovedAtBetweenAndStatus(startDateTime, endDateTime, DONE);

        return countWebhook == countPayment;

    }



}
