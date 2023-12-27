package com.wooyano.wooyanomonolithic.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooyano.wooyanomonolithic.reservation.application.reseravation.ReservationService;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.PaymentCompletionRequest;
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

  /*  @AfterEach
    void tearDown() {
        reservationRepository.deleteAllInBatch();
        reservationGoodsRepository.deleteAllInBatch();
    }*/

    @DisplayName("예약을 생성한다")
    @Test
    public void createReservation() throws Exception {
        // given
        ReservationGoods reservationGoods1 = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        ReservationGoods reservationGoods2 = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        reservationGoodsRepository.saveAll(List.of(reservationGoods1, reservationGoods2));

        ReservationCreateRequest request = ReservationCreateRequest.builder()
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
                .clientEmail("client@example.com")
                .build();
        // when
        ReservationCreateResponse reservations = reservationService.createReservation(request);
        // then
        assertThat(reservations.getOrderId()).isEqualTo(request.getOrderId());
    }

    @DisplayName("결제가 성공하면 예약데이터의 상태를 WAIT로 변경한다")
    @Test
    public void approveReservation(){
        // given
        ReservationGoods reservationGood = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        List<ReservationGoods> reservationGoods = List.of(reservationGood);
        LocalDate reservationDate = LocalDate.of(2023, 12, 31);
        LocalTime serviceStart = LocalTime.of(10, 0);
        LocalTime serviceEnd = LocalTime.of(12, 0);

        // when
        Reservation reservation = Reservation.createReservation(reservationGoods, "test@example.com",
                1L, 1L, reservationDate, serviceStart, serviceEnd, 50000, null, "요청사항",
                "서울시 강남구", "123456");

        PaymentCompletionRequest request = PaymentCompletionRequest.of(reservation);

        // when
        reservationService.approveReservation(request);
        // then

    }

}