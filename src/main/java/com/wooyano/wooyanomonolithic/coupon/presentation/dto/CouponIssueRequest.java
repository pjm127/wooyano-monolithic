package com.wooyano.wooyanomonolithic.coupon.presentation.dto;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponIssueRequest {
    private String name;

    public CouponIssueServiceRequest toServiceRequest(){
        return CouponIssueServiceRequest.builder()
                .name(name)
                .build();
    }
}
