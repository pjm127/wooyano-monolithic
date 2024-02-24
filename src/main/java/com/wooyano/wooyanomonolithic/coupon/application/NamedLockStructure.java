package com.wooyano.wooyanomonolithic.coupon.application;

import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.infrastructure.LockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class NamedLockStructure {

    private final LockRepository lockRepository;
    private final CouponService couponService;
    public NamedLockStructure(LockRepository lockRepository, CouponService couponService) {
        this.lockRepository = lockRepository;
        this.couponService = couponService;
    }
    @Transactional
    public void decrease(CouponIssueServiceRequest request) {
        Long id = request.getId();

        try {
            lockRepository.getLock(id.toString(),3000);
            couponService.issueCoupon(request);
        } finally {

            lockRepository.releaseLock(id.toString());
        }
    }
}
