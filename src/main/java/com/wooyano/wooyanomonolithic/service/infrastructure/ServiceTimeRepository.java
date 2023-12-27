package com.wooyano.wooyanomonolithic.service.infrastructure;

import com.wooyano.wooyanomonolithic.service.domain.ServiceTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTimeRepository extends JpaRepository<ServiceTime, Long> {
}
