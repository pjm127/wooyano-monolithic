package com.wooyano.wooyanomonolithic.reservation.application;

import com.wooyano.wooyanomonolithic.reservation.dto.ChangeReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.ReservationListResponse;
import java.util.List;


public interface ReservationService {


    // 서비스 신청(결제 요청 - 인증)
    List<CreateReservationResponse> createReservation(CreateReservationRequest reservationNewServiceRequest);

    //결제 승인 후 예약 상태 변경
    void approveReservation(ChangeReservationRequest request);

    // 클라이언트 별 예약 대기 상태 목록 조회
    List<ReservationListResponse>  findWaitReservationsList(Long serviceId);

}