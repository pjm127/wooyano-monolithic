package com.wooyano.wooyanomonolithic.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationGoodsRepositoryTest {

    @Autowired
    private ReservationGoodsRepository reservationGoodsRepository;

    @DisplayName("id로 예약 상품을 조회한다")
    @Test
    public void findById(){
        // given
        ReservationGoods reservationGoods = getReservationGoods();

        // when
        Optional<ReservationGoods> byId = reservationGoodsRepository.findById(reservationGoods.getId());
        // then
        assertThat(byId)
                .isPresent()
                .get()
                .extracting("id", "serviceItemName", "price", "serviceTime", "superCategory", "baseCategory", "subCategory")
                .contains(reservationGoods.getId(), reservationGoods.getServiceItemName(), reservationGoods.getPrice(),
                        reservationGoods.getServiceTime(), reservationGoods.getSuperCategory(), reservationGoods.getBaseCategory(),
                        reservationGoods.getSubCategory());


    }
    private ReservationGoods getReservationGoods() {
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
        String serviceItemName = "아이템1";
        int price = 5000;
        int serviceTime = 60;
        String superCategory = "스피너";
        String baseCategory = "스트레칭";
        String subCategory = "하체";
        ReservationGoods reservationGoods = ReservationGoods.createReservationGoods(
                serviceItemName, price, serviceTime, superCategory, baseCategory, subCategory, service
        );
        return reservationGoodsRepository.save(reservationGoods);

    }
}