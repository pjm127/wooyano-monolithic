package com.wooyano.wooyanomonolithic.coupon.infrastructure;

import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
}
