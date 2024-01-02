package com.wooyano.wooyanomonolithic.services.infrastructure;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTimeRepository extends JpaRepository<ServiceTime, Long> {
}
