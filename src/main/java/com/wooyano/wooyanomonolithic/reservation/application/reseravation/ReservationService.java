package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface ReservationService {


    // 서비스 신청(결제 요청 - 인증)
    ReservationResponse saveWorkTimeAndReservationAndPayment(String paymentKey, String orderId, int amount,
                                                             Long serviceId, Long workerId, String userEmail,
                                                             LocalDate reservationDate, String request, String address,
                                                             String clientEmail, LocalTime serviceStart, List<Long> reservationGoodsId);
    //결제 승인 후 예약 상태 변경
    void approveReservation(String orderId, Integer amount, String paymentKey);

    // 클라이언트 별 예약 대기 상태 목록 조회
    List<ReservationListResponse>  findWaitReservationsList(Long serviceId);

    //결제 실패 후 예약 취소
    void cancelReservation(String orderId);

}