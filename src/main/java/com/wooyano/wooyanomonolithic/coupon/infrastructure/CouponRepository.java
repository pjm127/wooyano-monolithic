package com.wooyano.wooyanomonolithic.coupon.infrastructure;

import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {

    @Query("select c from Coupon c where c.name = :name")
    Optional<Coupon> findByName(@Param("name") String name);
}
