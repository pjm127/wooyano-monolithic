package com.wooyano.wooyanomonolithic.coupon.infrastructure;

import com.wooyano.wooyanomonolithic.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LockRepository extends JpaRepository<Coupon, Long> {
    @Query(value = "select GET_LOCK(:key, :timeoutSeconds)", nativeQuery = true)
    void getLock(@Param("key") String key, @Param("timeoutSeconds") int timeoutSeconds);

    @Query(value = "SELECT release_lock(:key)", nativeQuery = true)
    void releaseLock(@Param("key") String key);
}
