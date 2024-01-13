package com.wooyano.wooyanomonolithic.payment.infrastructure;

import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResultResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


/*    @Query("SELECT new com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse(p.clientEmail,sum(p.totalAmount))"
            + " FROM Payment p WHERE p.createdAt BETWEEN :startDate AND :endDate group by p.clientEmail")
    List<PaymentResultResponse> findByApprovedAtAndPaymentStatus(@Param("startDate") LocalDateTime startDate,
                                                                 @Param("endDate") LocalDateTime endDate);*/

    Payment findByOrderId(String orderId);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.approvedAt BETWEEN :startDateTime AND :endDateTime"
            + " AND p.paymentStatus = :status")
    long countByApprovedAtBetweenAndStatus(@Param("startDateTime") LocalDateTime startDateTime,
                                  @Param("endDateTime") LocalDateTime endDateTime,
                                  @Param("status") PaymentStatus status);

}



