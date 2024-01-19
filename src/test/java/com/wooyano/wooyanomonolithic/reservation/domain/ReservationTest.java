package com.wooyano.wooyanomonolithic.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("예약 생성 시 주문 상태는 INIT이다.")
    @Test
    public void init(){
        // given
        List<ReservationGoods> reservationGoods = new ArrayList<>();
        String userEmail = "test@example.com";
        Long serviceId = 1L;
        Services service = Services.builder()
                .name("서비스1")
                .description("서비스1 설명")
                .build();
        Worker worker = Worker.create("작업자1", "작업자1 폰", "작업자1 설명", service);
        LocalDate reservationDate = LocalDate.of(2024, 1, 4);
        LocalTime serviceStart = LocalTime.of(10, 0);
        LocalTime serviceEnd = LocalTime.of(11, 0);
        int totalPrice = 10000;
        String cancelDesc = "취소 설명";
        String request = "요청사항";
        String address = "서울시 강남구";
        String orderId = "주문번호";
        LocalDateTime approvedAt = LocalDateTime.of(2024, 1, 4, 10, 0, 0);
        // when
        Reservation reservation = Reservation.createReservation(reservationGoods, userEmail, serviceId, worker,
                reservationDate, serviceStart, serviceEnd,totalPrice, cancelDesc, request, address, orderId,approvedAt);
        // then
        assertThat(reservation.getReservationState()).isEqualByComparingTo(ReservationState.WAIT);
    }

}