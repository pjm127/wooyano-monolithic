package com.wooyano.wooyanomonolithic.reservation.dto;


import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReservationListResponse {

    private ReservationGoods reservationGoods;
    private String userEmail;
    private Long serviceId;
    private Long workerId;
    private LocalDate reservationDate;
    private LocalTime serviceStart;
    private LocalTime serviceEnd;
    private ReservationState reservationState;
    private Integer paymentAmount;
    private String cancelDesc;
    private String request;
    private String reservationNum;
    private String address;

}
