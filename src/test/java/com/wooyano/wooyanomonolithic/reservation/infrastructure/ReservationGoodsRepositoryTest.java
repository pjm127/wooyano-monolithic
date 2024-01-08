package com.wooyano.wooyanomonolithic.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationGoodsRepositoryTest {

    @Autowired
    private ReservationGoodsRepository reservationGoodsRepository;

    @Autowired
    private ServicesRepository servicesRepository;



    @DisplayName("id로 예약 상품을 조회한다")
    @Test
    public void findById(){
        // given
        ReservationGoods reservationGoods = getReservationGoods();

        // when
        Optional<ReservationGoods> reservationGood = reservationGoodsRepository.findById(reservationGoods.getId());
        // then
        assertThat(reservationGood)
                .isPresent()
                .get()
                .extracting("id", "serviceItemName", "price" ,"superCategory", "baseCategory", "subCategory")
                .contains(reservationGoods.getId(), reservationGoods.getServiceItemName(), reservationGoods.getPrice()
                        , reservationGoods.getSuperCategory(), reservationGoods.getBaseCategory(), reservationGoods.getSubCategory());


    }

    @DisplayName("예약 상품 id 리스트로 예약 상품을 조회한다")
    @Test
    public void findByIdIn(){
        // given
        getReservationGoodsList();
        List<Long> reservationGoodsIdList = List.of(2l, 3l);

        // when
        List<ReservationGoods> reservationGoodsList = reservationGoodsRepository.findByIdIn(reservationGoodsIdList);
        // then
        assertThat(reservationGoodsList).hasSize(2)
                .extracting("serviceItemName", "price")
                .containsExactlyInAnyOrder(
                        tuple("아이템1", 5000),
                        tuple("아이템2", 5000)
                );

    }

    private List<ReservationGoods> getReservationGoodsList() {
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
        String serviceItemName1 = "아이템1";
        String serviceItemName2 = "아이템2";
        int price = 5000;
        String superCategory = "가전제품";
        String baseCategory = "에어컨";
        String subCategory = "시스템에어컨";
        ReservationGoods reservationGoods1 = ReservationGoods.createReservationGoods(
                serviceItemName1, price, superCategory, baseCategory, subCategory, service
        );
        ReservationGoods reservationGoods2 = ReservationGoods.createReservationGoods(
                serviceItemName2, price, superCategory, baseCategory, subCategory, service
        );
        return reservationGoodsRepository.saveAll(List.of(reservationGoods1, reservationGoods2));
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
        servicesRepository.save(service);
        String serviceItemName = "아이템1";
        int price = 5000;
        String superCategory = "가전제품";
        String baseCategory = "에어컨";
        String subCategory = "시스템에어컨";
        ReservationGoods reservationGoods = ReservationGoods.createReservationGoods(
                serviceItemName, price, superCategory, baseCategory, subCategory, service
        );
        return reservationGoodsRepository.save(reservationGoods);

    }
}