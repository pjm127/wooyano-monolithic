package com.wooyano.wooyanomonolithic.reservation.dto;

import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreateReservationRequest {

    private List<Long> reservationGoodsId; //예약 상품id
    private String orderId; //주문번호
    private Long serviceId; //서비스id
    private Long workerId; //작업자id
    private String userEmail; //유저 이메일
    private LocalDate reservationDate; //예약 날짜
    private LocalTime serviceStart; // 서비스 시작 시간
    private LocalTime serviceEnd;  // 서비스 종료 시간
    private Integer paymentAmount; //결제 금액
    private String request; //요청사항
    private String address; //주소
    private String clientEmail; //사업자 이메일


}
