package com.wooyano.wooyanomonolithic.worker.infrastructure;

import com.wooyano.wooyanomonolithic.worker.domain.WorkReservedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkReservedProductRepository extends JpaRepository<WorkReservedProduct, Long> {

    @Query("select w from WorkReservedProduct w where w.reservationGoods.id = :reservationGoodsId and w.worker.id = :workerId")
    WorkReservedProduct findByReservationGoodsAndWorker(Long reservationGoodsId, Long workerId);

}