package com.wooyano.wooyanomonolithic.worker.infrastructure;

import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerTimeRepository  extends JpaRepository<WorkerTime, Long> {

    Optional<WorkerTime> findByWorkerId(Long workerId);
}
