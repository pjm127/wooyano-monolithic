package com.wooyano.wooyanomonolithic.reservation.infrastructure;


import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReservationRepository extends JpaRepository<Reservation, Long>  {

    //@Lock(PESSIMISTIC_WRITE)
/*    @Query(value = "SELECT r FROM Reservation r join r.reservationGoods g where g.id = :reservationGoodsId  and r.workerId = :workerId")
    Optional<Reservation> findByReservationGoodsId(@Param("reservationGoodsId") Long reservationGoodsId, @Param("workerId") Long workerId);*/


/*    @Query("SELECT r FROM Reservation r WHERE r.orderId = :orderId")
   Reservation findByOrderId(@Param("orderId") String orderId); //findByOrderId*/

    @Query("SELECT r FROM Reservation r WHERE r.orderId = :orderId")
    Reservation findByOrderIdList(@Param("orderId") String orderId);

    //@Query("SELECT r FROM Reservation r join fetch r.reservationGoods g WHERE r.serviceId = :serviceId and r.reservationState = :reservationState")
   // List<Reservation> findByReservationStatusWait(@Param("serviceId") Long serviceId, @Param("reservationState") ReservationState reservationState);

}
