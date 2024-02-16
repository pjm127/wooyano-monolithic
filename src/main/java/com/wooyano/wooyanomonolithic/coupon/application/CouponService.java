package com.wooyano.wooyanomonolithic.coupon.application;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponResponse;

public interface CouponService {
    void issueCoupon(CouponIssueServiceRequest request);
}
