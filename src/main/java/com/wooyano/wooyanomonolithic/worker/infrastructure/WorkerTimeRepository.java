package com.wooyano.wooyanomonolithic.worker.infrastructure;

import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkerTimeRepository  extends JpaRepository<WorkerTime, Long> {

   // @Lock(LockModeType.NONE)
    @Query("select wt from WorkerTime wt "
            + "where wt.worker = :worker and wt.serviceTime = :serviceTime")
    Optional<WorkerTime> findByWorkerAndServiceTime(@Param("worker") Worker worker,
                                                    @Param("serviceTime") LocalTime serviceTime);

}
