package com.wooyano.wooyanomonolithic.reservation.application.reservationGoods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsResponse;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationGoodsServiceTest {

    @Autowired
    private ReservationGoodsService reservationGoodsService;
    @Autowired
    private ServicesRepository servicesRepository;

    @DisplayName("예약 상품을 생성한다")
    @Test
    public void createReservationGoods(){
        // given
        LocalTime openTime = LocalTime.of(9, 0, 0);
        LocalTime closeTime = LocalTime.of(18, 0, 0);
        ServiceTime servicesTime = ServiceTime.builder()
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
        Services service = Services.builder()
                .name("서비스1")
                .description("서비스1 설명")
                .serviceTime(servicesTime)
                .build();
        servicesRepository.save(service);

        ReservationGoodsCreateRequest request = ReservationGoodsCreateRequest.builder()
                .serviceId(1l)
                .serviceItemName("아이템1")
                .price(5000)
                .superCategory("가전제품")
                .baseCategory("에어컨")
                .subCategory("시스템에어컨")
                .build();

        // when
        ReservationGoodsResponse reservationGoods = reservationGoodsService.createReservationGoods(request);
        // then
        assertThat(reservationGoods.getId()).isNotNull();
        assertThat(reservationGoods)
                .extracting("serviceItemName", "price", "superCategory", "baseCategory", "subCategory")
                .contains("아이템1", 5000, "가전제품", "에어컨", "시스템에어컨");

    }
}