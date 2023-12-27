package com.wooyano.wooyanomonolithic.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationGoodsRepository reservationGoodsRepository;

    @DisplayName("예약상품id로 예약을 조회한다")
    @Test
    public void findByReservationGoodsId(){
        // given
        ReservationGoods reservationGood = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        reservationGoodsRepository.save(reservationGood);
        List<ReservationGoods> reservationGoods = List.of(reservationGood);

        LocalDate reservationDate = LocalDate.of(2023, 12, 31);
        LocalTime serviceStart = LocalTime.of(10, 0);
        LocalTime serviceEnd = LocalTime.of(12, 0);
        Reservation reservation = Reservation.createReservation(reservationGoods, "test@example.com",
                1L, 1L, reservationDate, serviceStart, serviceEnd, 50000, null, "요청사항",
                "서울시 강남구", "wer123t456");
        reservationRepository.save(reservation);

        ReservationGoods reservationGoods1 = reservationGoods.get(0);
        Long id = reservationGoods1.getId();
        // when
        Optional<Reservation> result = reservationRepository.findByReservationGoodsId(id, 1L);

        // then
        assertThat(result).isPresent();
        Reservation foundReservation = result.get();
        assertThat(foundReservation.getWorkerId()).isEqualTo(1L);
    }

/*    @DisplayName("주문번호로 예약리스트를 조회한다")
    @Test
    public void 테스트이름(){
        // given
        ReservationGoods reservationGood = ReservationGoods.createReservationGoods(1l, "test", 30000,
                5, "시스템", "가전제품", "에어컨");
        List<ReservationGoods> reservationGoods = List.of(reservationGood);

        LocalDate reservationDate = LocalDate.of(2023, 12, 31);
        LocalTime serviceStart = LocalTime.of(10, 0);
        LocalTime serviceEnd = LocalTime.of(12, 0);
        Reservation reservation1 = Reservation.createReservation(reservationGoods, "test@example.com",
                1L, 1L, reservationDate, serviceStart, serviceEnd, 50000, null, "요청사항",
                "서울시 강남구", "wer123t456");
        Reservation reservation2 = Reservation.createReservation(reservationGoods, "test@example.com",
                1L, 2L, reservationDate, serviceStart, serviceEnd, 50000, null, "요청사항",
                "서울시 강남구", "wer123t456");
        reservationRepository.saveAll(List.of(reservation1,reservation2));
        // when
        List<Reservation> reservations = reservationRepository.findByOrderIdList(reservation1.getOrderId());
        // then
        assertThat(reservations).hasSize(2)
                .extracting("workerId","orderId","serviceId")
                .containsExactlyInAnyOrder(
                        tuple(1L,"wer123t456",1L),
                        tuple(2L,"wer123t456",1L)
                );
    }*/


}