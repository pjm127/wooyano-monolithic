package com.wooyano.wooyanomonolithic.coupon.infrastructure;

import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    Optional<Coupon> findByName(String name);
}
