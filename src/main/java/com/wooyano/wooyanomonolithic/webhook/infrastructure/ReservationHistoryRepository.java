package com.wooyano.wooyanomonolithic.webhook.infrastructure;

import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.webhook.domain.ReservationHistoryWebhook;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistoryWebhook,Long> {

    @Query("SELECT COUNT(e) FROM ReservationHistoryWebhook e WHERE e.approvedAt BETWEEN :startDateTime AND :endDateTime"
            + " AND e.paymentStatus = :status")
    long countByApprovedAtBetween(@Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime,
                                  @Param("status") PaymentStatus status);
}
