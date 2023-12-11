package com.wooyano.wooyanomonolithic.reservation.infrastructure;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ReservationGoodsRepository extends JpaRepository<ReservationGoods, Long> {

    Optional<ReservationGoods> findById(Long id);
    List<ReservationGoods> findByServiceIdOrderByIdDesc(Long serviceId);


}
