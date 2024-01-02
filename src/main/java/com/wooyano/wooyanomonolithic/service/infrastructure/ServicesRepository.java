package com.wooyano.wooyanomonolithic.service.infrastructure;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    @Query("select s from Services s join fetch s.workers w "
            + "join fetch s.serviceTime st "
            + "where s.id = :id")
    Services findByIdWithWorkers(@Param("id") Long id);

}
