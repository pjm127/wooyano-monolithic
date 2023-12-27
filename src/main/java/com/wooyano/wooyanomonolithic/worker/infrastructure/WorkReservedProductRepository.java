package com.wooyano.wooyanomonolithic.worker.infrastructure;

import com.wooyano.wooyanomonolithic.worker.domain.WorkReservedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkReservedProductRepository extends JpaRepository<WorkReservedProduct, Long> {

}
