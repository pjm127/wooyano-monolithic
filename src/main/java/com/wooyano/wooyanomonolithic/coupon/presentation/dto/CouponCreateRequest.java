package com.wooyano.wooyanomonolithic.coupon.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    private int totalQuantity; // 쿠폰 수량
}
