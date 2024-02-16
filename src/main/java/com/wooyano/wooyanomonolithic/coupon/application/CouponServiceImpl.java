package com.wooyano.wooyanomonolithic.coupon.application;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponResponse;
import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import com.wooyano.wooyanomonolithic.coupon.infrastructure.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public void issueCoupon(CouponIssueServiceRequest request) {
        String name = request.getName();
        Coupon coupon = couponRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        coupon.decreaseQuantity();

    }
}
