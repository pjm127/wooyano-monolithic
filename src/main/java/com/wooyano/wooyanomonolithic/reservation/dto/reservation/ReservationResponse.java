package com.wooyano.wooyanomonolithic.reservation.dto.reservation;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationResponse {
    private Long reservationId;
    private String orderId;
    private int amount;
    private Long serviceId;
    private String userEmail;
    private LocalDate reservationDate;
    private String request;
    private String address;
    private LocalTime serviceStart;
    private Long workerId;

    @Builder
    private ReservationResponse(Long reservationId, String orderId, int amount, Long serviceId, String userEmail,
                               LocalDate reservationDate, String request, String address, LocalTime serviceStart,
                               Long workerId) {
        this.reservationId = reservationId;
        this.orderId = orderId;
        this.amount = amount;
        this.serviceId = serviceId;
        this.userEmail = userEmail;
        this.reservationDate = reservationDate;
        this.request = request;
        this.address = address;
        this.serviceStart = serviceStart;
        this.workerId = workerId;
    }

    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .orderId(reservation.getOrderId())
                .amount(reservation.getTotalPrice())
                .serviceId(reservation.getServiceId())
                .userEmail(reservation.getUserEmail())
                .reservationDate(reservation.getReservationDate())
                .request(reservation.getRequest())
                .address(reservation.getAddress())
                .serviceStart(reservation.getServiceStart())
                .workerId(reservation.getWorker().getId())
                .build();
    }


}
