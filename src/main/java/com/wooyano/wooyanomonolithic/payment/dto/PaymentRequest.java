package com.wooyano.wooyanomonolithic.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PaymentRequest {
    private String clientEmail; //사업자 이메일

    @Schema(description = "결제 수단")
    private String payType; //있
    @Schema(description = "총 결제 금액")
    private int totalAmount; //있

    @Schema(description = "결제 승인이 일어난 날짜와 시간 정보")
    private LocalDateTime approvedAt; //있

    @Schema(description = "결제 처리 상태")
    private String payStatus;
}
