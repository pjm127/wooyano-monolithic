package com.wooyano.wooyanomonolithic.reservation.dto.reservation;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationCreateResponse {

    private Long id;
    private String orderId;
    private LocalDate reservationDate;
    private List<ReservationGoods> reservationGoods;

    @Builder
    private ReservationCreateResponse(Long id, String orderId, LocalDate reservationDate, List<ReservationGoods> reservationGoods) {
        this.id = id;
        this.orderId = orderId;
        this.reservationDate = reservationDate;
        this.reservationGoods = reservationGoods;
    }

    public static ReservationCreateResponse of(Reservation reservation) {
        return ReservationCreateResponse.builder()
                .id(reservation.getId())
                .orderId(reservation.getOrderId())
                .reservationDate(reservation.getReservationDate())
                .build();

    }



}
