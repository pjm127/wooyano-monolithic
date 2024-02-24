package com.wooyano.wooyanomonolithic.coupon.presentation.dto;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponIssueRequest {
    private Long id;

    public CouponIssueServiceRequest toServiceRequest(){
        return CouponIssueServiceRequest.builder()
                .id(id)
                .build();
    }
}
