package com.wooyano.wooyanomonolithic.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import com.wooyano.wooyanomonolithic.coupon.infrastructure.CouponRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;

    @DisplayName("쿠폰발급하고 개수를 1개 차감한다.")
    @Test
    public void issueCoupon(){
        // given
        Coupon coupon = Coupon.builder()
                .name("test1")
                .totalQuantity(100)
                .build();
        couponRepository.save(coupon);
        CouponIssueServiceRequest request = CouponIssueServiceRequest.builder()
                .name("test1")
                .build();
        // when
        couponService.issueCoupon(request);

        // then
        Coupon findCoupon = couponRepository.findByName(request.getName()).get();
        assertThat(findCoupon.getTotalQuantity()).isEqualTo(99);
    }
}