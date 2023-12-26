package com.wooyano.wooyanomonolithic.reservation.dto;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateReservationResponse {

    private Long id;
    private String orderId;
    private LocalDateTime registeredDateTime;

    @Builder
    private CreateReservationResponse(Long id, String orderId, LocalDateTime registeredDateTime) {
        this.id = id;
        this.orderId = orderId;
        this.registeredDateTime = registeredDateTime;
    }

    public static List<CreateReservationResponse> of(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservationEntity -> CreateReservationResponse.builder()
                        .id(reservationEntity.getId())
                        .orderId(reservationEntity.getOrderId())
                        .registeredDateTime(reservationEntity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }



}
