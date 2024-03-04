package com.wooyano.wooyanomonolithic.coupon.application.structure;

import com.wooyano.wooyanomonolithic.coupon.application.CouponService;
import com.wooyano.wooyanomonolithic.coupon.application.dto.CouponIssueServiceRequest;
import com.wooyano.wooyanomonolithic.coupon.infrastructure.LockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NamedLockStructure {

    private final LockRepository lockRepository;
    private final CouponService couponService;

    @Transactional
    public void issueCoupon(CouponIssueServiceRequest request) {
        Long id = request.getId();

        try {
            lockRepository.getLock(id.toString(),3000);
            couponService.issueCoupon(request);
        } finally {

            lockRepository.releaseLock(id.toString());
        }
    }
}
