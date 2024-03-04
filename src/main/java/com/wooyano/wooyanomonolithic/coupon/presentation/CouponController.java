package com.wooyano.wooyanomonolithic.coupon.presentation;

import com.wooyano.wooyanomonolithic.coupon.application.CouponService;
import com.wooyano.wooyanomonolithic.coupon.application.structure.NamedLockStructure;
import com.wooyano.wooyanomonolithic.coupon.presentation.dto.CouponIssueRequest;
import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    // private final CouponService couponService;

    private final NamedLockStructure namedLockStructure;

    @PostMapping("/issue")
    public ResponseEntity<String> issueCoupon(@RequestBody CouponIssueRequest request){
        namedLockStructure.issueCoupon(request.toServiceRequest());
        return ResponseEntity.ok("쿠폰 발급");
    }


}
