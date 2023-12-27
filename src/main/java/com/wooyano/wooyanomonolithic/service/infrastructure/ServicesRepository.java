package com.wooyano.wooyanomonolithic.service.infrastructure;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesRepository extends JpaRepository<Services, Long> {
}
