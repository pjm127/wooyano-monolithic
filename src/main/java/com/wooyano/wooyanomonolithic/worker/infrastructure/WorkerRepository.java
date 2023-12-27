package com.wooyano.wooyanomonolithic.worker.infrastructure;

import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByServiceId(Long serviceId);
}
