package com.wooyano.wooyanomonolithic.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        Coupon coupon = createCoupon();
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

    @DisplayName("쿠폰 이름이 존재하지 않으면 예외를 발생시킨다.")
    @Test
    public void issueCouponWithNoName(){
        // given
        Coupon coupon = createCoupon();
        couponRepository.save(coupon);
        CouponIssueServiceRequest request = CouponIssueServiceRequest.builder()
                .name("test12")
                .build();
        // when// then
        assertThatThrownBy(() -> couponService.issueCoupon(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 쿠폰입니다.");
    }

    private Coupon createCoupon(){
        return Coupon.builder()
                .name("test1")
                .totalQuantity(100)
                .build();
    }
}