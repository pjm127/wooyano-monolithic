package com.wooyano.wooyanomonolithic.payment.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentRequest {
    private int totalAmount;
    private String orderId; //주문번호
    private String clientEmail; //주문자 이메일

}
