package com.wooyano.wooyanomonolithic.reservation.application;

import com.wooyano.wooyanomonolithic.reservation.dto.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.ReservationListResponse;
import java.util.List;


public interface ReservationService {


    // 서비스 신청(결제 요청 - 인증)
    CreateReservationResponse createReservation(CreateReservationRequest reservationNewServiceRequest);

    //결제 승인 후 예약 상태 변경
    void approveReservation(PaymentCompletionRequest request);

    // 클라이언트 별 예약 대기 상태 목록 조회
    List<ReservationListResponse>  findWaitReservationsList(Long serviceId);

    //결제 실패 후 예약 취소
    void cancelReservation(String orderId);

}