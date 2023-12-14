package com.wooyano.wooyanomonolithic.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentRequest {
    private int totalAmount;
    private String orderId; //주문번호

}
