package com.wooyano.wooyanomonolithic.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @DisplayName("예약 초기 생성 시 상태는 PAYMENT_WAITING이다.")
    @Test
    public void initCreateReservation(){
        // given
        ReservationGoods reservationGoods = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        LocalDate reservationDate = LocalDate.of(2023, 12, 31);
        LocalTime serviceStart = LocalTime.of(10, 0);
        LocalTime serviceEnd = LocalTime.of(12, 0);

        // when
        Reservation reservation = Reservation.createReservation(reservationGoods, "test@example.com",
                1L, 1L, reservationDate, serviceStart, serviceEnd, 50000, null, "요청사항",
                "서울시 강남구", "123456");
        // then
        assertThat(reservation.getReservationState()).isEqualByComparingTo(ReservationState.PAYMENT_WAITING);
    }
}