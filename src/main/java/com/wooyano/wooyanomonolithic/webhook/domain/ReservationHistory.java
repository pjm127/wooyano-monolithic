package com.wooyano.wooyanomonolithic.webhook.domain;

import jakarta.persistence.Entity;
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
public class ReservationHistory {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String orderId;
    private LocalDateTime createdAt;

    @Builder
    private ReservationHistory(String orderId, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public static ReservationHistory create(String orderId, LocalDateTime createdAt) {
        return ReservationHistory.builder()
            .orderId(orderId)
            .createdAt(createdAt)
            .build();
    }
}
