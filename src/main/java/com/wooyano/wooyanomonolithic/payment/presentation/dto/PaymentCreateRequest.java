package com.wooyano.wooyanomonolithic.payment.presentation.dto;

import com.wooyano.wooyanomonolithic.payment.application.dto.PaymentCreateServiceRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public class PaymentCreateRequest {

    private String orderId; //주문번호 77
    private Long serviceId; //서비스id 77
    private Long workerId; //작업자id 77
    private LocalDate reservationDate; //예약 날짜 77
    private LocalTime serviceStartTime; // 서비스 시작 시간 77
    private int paymentAmount; //결제 금액 77

    @Builder
    private PaymentCreateRequest(String orderId, Long serviceId, Long workerId, LocalDate reservationDate,
                                LocalTime serviceStartTime, int paymentAmount) {
        this.orderId = orderId;
        this.serviceId = serviceId;
        this.workerId = workerId;
        this.reservationDate = reservationDate;
        this.serviceStartTime = serviceStartTime;
        this.paymentAmount = paymentAmount;
    }

    public PaymentCreateServiceRequest toServiceRequest(){
        return PaymentCreateServiceRequest.builder()
                .orderId(orderId)
                .serviceId(serviceId)
                .workerId(workerId)
                .reservationDate(reservationDate)
                .serviceStartTime(serviceStartTime)
                .paymentAmount(paymentAmount)
                .build();
    }
}
