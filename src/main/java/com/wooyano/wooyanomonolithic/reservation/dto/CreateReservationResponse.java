package com.wooyano.wooyanomonolithic.reservation.dto;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateReservationResponse {

    private Long id;
    private String orderId;
    private LocalDate reservationDate;
    private List<ReservationGoods> reservationGoods;

    @Builder
    private CreateReservationResponse(Long id, String orderId, LocalDate reservationDate, List<ReservationGoods> reservationGoods) {
        this.id = id;
        this.orderId = orderId;
        this.reservationDate = reservationDate;
        this.reservationGoods = reservationGoods;
    }

    public static CreateReservationResponse of(Reservation reservation) {
        return CreateReservationResponse.builder()
                .id(reservation.getId())
                .orderId(reservation.getOrderId())
                .reservationDate(reservation.getReservationDate())
                .build();

    }



}
