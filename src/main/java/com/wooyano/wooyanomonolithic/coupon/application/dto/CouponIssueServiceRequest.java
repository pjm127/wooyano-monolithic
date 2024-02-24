package com.wooyano.wooyanomonolithic.coupon.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponIssueServiceRequest {
    private Long id;

    @Builder
    private CouponIssueServiceRequest(Long id) {
        this.id = id;
    }
}
