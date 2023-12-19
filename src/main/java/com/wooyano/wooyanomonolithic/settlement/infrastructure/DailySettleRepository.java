package com.wooyano.wooyanomonolithic.settlement.infrastructure;

import com.wooyano.wooyanomonolithic.settlement.domain.DailySettle;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailySettleRepository extends JpaRepository<DailySettle, Long>,DailySettleRepositoryCustom {
    Optional<DailySettle> findByClientEmail(String clientEmail);

    @Query("SELECT SUM(d.totalAmount) as totalAmountSum FROM DailySettle d WHERE d.clientEmail= :clientEmail and d.settlementDate between :startDate AND :endDate group by d.clientEmail")
    Long sumTotalAmountByClientEmailAndSettlementDate(@Param("clientEmail") String clientEmail,
                                                      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
