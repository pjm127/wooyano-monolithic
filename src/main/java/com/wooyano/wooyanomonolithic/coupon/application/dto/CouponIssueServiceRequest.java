package com.wooyano.wooyanomonolithic.coupon.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponIssueServiceRequest {
    private String name;

    @Builder
    private CouponIssueServiceRequest(String name) {
        this.name = name;
    }
}
