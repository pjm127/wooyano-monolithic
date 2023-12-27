package com.wooyano.wooyanomonolithic.reservation.dto.reservation;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class PaymentCompletionRequest {

    private String orderId; //주문번호


    @Builder
    private PaymentCompletionRequest(String orderId) {
        this.orderId = orderId;
    }

    public static PaymentCompletionRequest of(Reservation reservation) {
        return PaymentCompletionRequest.builder()
                .orderId(reservation.getOrderId())
                .build();
    }
}
