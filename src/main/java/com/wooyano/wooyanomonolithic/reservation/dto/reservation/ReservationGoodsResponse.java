package com.wooyano.wooyanomonolithic.reservation.dto.reservation;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.service.domain.Services;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationGoodsResponse {

    private Long id;

    private String serviceItemName;
    private Integer price;
    private Integer serviceTime;
    private String superCategory;
    private String baseCategory;
    private String subCategory;
    private Services service;

    @Builder
    private ReservationGoodsResponse(Long id, String serviceItemName, Integer price, Integer serviceTime,
                                    String superCategory, String baseCategory, String subCategory,Services service) {
        this.id = id;
        this.serviceItemName = serviceItemName;
        this.price = price;
        this.serviceTime = serviceTime;
        this.superCategory = superCategory;
        this.baseCategory = baseCategory;
        this.subCategory = subCategory;
        this.service = service;
    }

    public static ReservationGoodsResponse of(ReservationGoods reservationGoods) {
        return ReservationGoodsResponse.builder()
                .id(reservationGoods.getId())
                .serviceItemName(reservationGoods.getServiceItemName())
                .price(reservationGoods.getPrice())
                .serviceTime(reservationGoods.getServiceTime())
                .superCategory(reservationGoods.getSuperCategory())
                .baseCategory(reservationGoods.getBaseCategory())
                .subCategory(reservationGoods.getSubCategory())
                .service(reservationGoods.getService())
                .build();
    }
}
