package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ReservationService {

   void checkWorkerAvailability(Worker worker,LocalDate reservationDate,LocalTime serviceStart);
    void verifyPayment(String orderId, int amount);



    // 서비스 신청(결제 요청 - 인증)
    ReservationResponse saveWorkTimeAndReservationAndPayment(String paymentKey, String orderId, int amount,
                                                             Long serviceId, Long workerId, String userEmail,
                                                             LocalDate reservationDate, String request, String address,
                                                             String clientEmail, LocalTime serviceStart,LocalTime serviceEnd,
                                                             List<Long> reservationGoodsId, int suppliedAmount, int vat,
                                                             String status, String method,
                                                             Worker worker, String approvedAt);


    // 클라이언트 별 예약 대기 상태 목록 조회
    List<ReservationListResponse>  findWaitReservationsList(Long serviceId);

    //결제 실패 후 예약 취소
    void cancelReservation(String orderId);

}