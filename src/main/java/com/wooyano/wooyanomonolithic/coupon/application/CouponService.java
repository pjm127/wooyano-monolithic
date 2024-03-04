package com.wooyano.wooyanomonolithic.coupon.application;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import com.wooyano.wooyanomonolithic.coupon.infrastructure.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void issueCoupon(CouponIssueServiceRequest request) {
        Long id= request.getId();
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        coupon.decreaseQuantity();

    }
}
