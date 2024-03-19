package com.wooyano.wooyanomonolithic.worker.infrastructure;

import static jakarta.persistence.LockModeType.OPTIMISTIC;

import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkerTimeRepository  extends JpaRepository<WorkerTime, Long> {

    @Lock(OPTIMISTIC)
    @Query("select wt from WorkerTime wt "
            + "where wt.worker = :worker and :serviceTime BETWEEN wt.serviceStartTime AND wt.serviceEndTime and wt.registeredDate = :registeredDate")
    Optional<WorkerTime> findByWorkerAndServiceTime(@Param("worker") Worker worker,
                                                    @Param("serviceTime") LocalTime serviceTime,
                                                    @Param("registeredDate") LocalDate registeredDate);
}
