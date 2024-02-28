package com.wooyano.wooyanomonolithic.settlement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleResult {
    private String clientEmail; //사업자 이메일

    private long totalAmount; //결제 금액
    private long fee; //수수료
    private long payOutAmount; //지급 금액입니다. 결제 금액 amount에서 수수료인 fee를 제외한 금액입니다.


}