package com.wooyano.wooyanomonolithic.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationServiceImplTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationGoodsRepository reservationGoodsRepository;


    @DisplayName("예약을 생성한다")
    @Test
    public void createReservation() throws Exception {
        // given
        ReservationGoods reservationGoods1 = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        ReservationGoods reservationGoods2 = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        reservationGoodsRepository.saveAll(List.of(reservationGoods1, reservationGoods2));

        CreateReservationRequest request = CreateReservationRequest.builder()
                .reservationGoodsId(List.of(1L, 2L))
                .orderId("ewas123456")
                .serviceId(1L)
                .workerId(2L)
                .userEmail("user@example.com")
                .reservationDate(LocalDate.of(2023, 12, 31))
                .serviceStart(LocalTime.of(10, 0))
                .serviceEnd(LocalTime.of(12, 0))
                .paymentAmount(50000)
                .request("Special request")
                .address("Seoul, Korea")
                .status(1)
                .clientEmail("client@example.com")
                .build();
        // when
        List<CreateReservationResponse> reservations = reservationService.createReservation(request);
        // then
        assertThat(reservations).hasSize(2)
                .extracting("id", "orderId")
                .containsExactlyInAnyOrder(
                        tuple(1L, "ewas123456"),
                        tuple(2L, "ewas123456")
                );

    }

}