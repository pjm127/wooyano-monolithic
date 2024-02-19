package com.wooyano.wooyanomonolithic.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import com.wooyano.wooyanomonolithic.coupon.infrastructure.CouponRepository;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
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

    @DisplayName("존재하지 않는 이름의 쿠폰 발급을 요청하면 예외를 발생시킨다.")
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

//    @DisplayName("쿠폰 발급시 수량이 떨어지면 예외를 발생한다")
//    @Test
//    public void issueCouponWithNoQuantity(){
//        // given
//        Coupon coupon = createCoupon();
//        couponRepository.save(coupon);
//        CouponIssueServiceRequest request = CouponIssueServiceRequest.builder()
//                .name("test1")
//                .build();
//        // when
//        for (int i = 0; i < 101; i++) {
//            couponService.issueCoupon(request);
//        }
//        // then
//    }

    @DisplayName("쿠폰 발급 동시에 100개를 요청한다")
    @Test
    public void issueCoupon_concurrency() throws InterruptedException {
        // given
        Coupon coupon = createCoupon();
        couponRepository.save(coupon);
        CouponIssueServiceRequest request = CouponIssueServiceRequest.builder()
                .name("test1")
                .build();

        int threadSize = 100;
        var executorService = Executors.newFixedThreadPool(threadSize);
        var countDownLatch = new CountDownLatch(threadSize);

        //when
        for (int i = 0; i < threadSize; i++) {
            executorService.submit(() -> {
                couponService.issueCoupon(request);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        Coupon findCoupon = couponRepository.findByName(request.getName()).get();
        assertThat(findCoupon.getTotalQuantity()).isEqualTo(0);
    }

    private Coupon createCoupon(){
        return Coupon.builder()
                .name("test1")
                .totalQuantity(100)
                .build();
    }
}