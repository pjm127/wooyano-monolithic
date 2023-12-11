package com.wooyano.wooyanomonolithic.reservation.infrastructure;


import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReservationRepository extends JpaRepository<Reservation, Long>  {

    @Query(value = "SELECT r FROM Reservation r WHERE r.reservationGoods.id = :reservationGoods and r.workerId = :workerId")
    Optional<Reservation> findByReservationGoodsId(@Param("reservationGoods") Long reservationGoods, @Param("workerId") Long workerId);



    @Query("SELECT r FROM Reservation r WHERE r.orderId = :orderId")
   Reservation findByOrderId(@Param("orderId") String orderId); //findByOrderId

    @Query("SELECT r FROM Reservation r WHERE r.orderId = :orderId")
    List<Reservation> findByOrderIdList(@Param("orderId") String orderId);

    @Query("SELECT r FROM Reservation r join fetch r.reservationGoods g WHERE r.serviceId = :serviceId and r.reservationState = :reservationState")
    List<Reservation> findByReservationStatusWait(@Param("serviceId") Long serviceId, @Param("reservationState") ReservationState reservationState);

}
