package com.wooyano.wooyanomonolithic.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResultResponse {

    private String clientEmail; //사업자 이메일

    private long totalAmount; //있

}
