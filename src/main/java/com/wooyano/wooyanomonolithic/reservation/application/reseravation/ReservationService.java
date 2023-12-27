package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import com.wooyano.wooyanomonolithic.reservation.dto.reservation.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import java.util.List;


public interface ReservationService {


    // 서비스 신청(결제 요청 - 인증)
    ReservationCreateResponse createReservation(ReservationCreateRequest reservationNewServiceRequest);

    //결제 승인 후 예약 상태 변경
    void approveReservation(PaymentCompletionRequest request);

    // 클라이언트 별 예약 대기 상태 목록 조회
    List<ReservationListResponse>  findWaitReservationsList(Long serviceId);

    //결제 실패 후 예약 취소
    void cancelReservation(String orderId);

}