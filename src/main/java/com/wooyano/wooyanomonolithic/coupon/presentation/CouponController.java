package com.wooyano.wooyanomonolithic.coupon.presentation;

import com.wooyano.wooyanomonolithic.coupon.application.CouponService;
import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponResponse;
import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/issue")
    public BaseResponse<?> issueCoupon(){
        CouponResponse couponResponse = couponService.issueCoupon();
        return new BaseResponse<>(couponResponse);
    }


}
