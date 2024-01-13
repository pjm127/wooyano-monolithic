package com.wooyano.wooyanomonolithic.webhook.domain;

import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatusConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationHistoryWebhook {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private LocalDateTime approvedAt;

    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus;
    @Builder
    private ReservationHistoryWebhook(String orderId, LocalDateTime approvedAt, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.approvedAt = approvedAt;
        this.paymentStatus = paymentStatus;
    }

    public static ReservationHistoryWebhook create(String orderId, LocalDateTime approvedAt, PaymentStatus paymentStatus) {
        return ReservationHistoryWebhook.builder()
            .orderId(orderId)
            .approvedAt(approvedAt)
            .paymentStatus(paymentStatus)
            .build();
    }
}
