package com.wooyano.wooyanomonolithic.reservation.dto;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationGoodsResponse {

    private Long id;
    private Long serviceId;
    private String serviceItemName;
    private Integer price;
    private Integer serviceTime;
    private String superCategory;
    private String baseCategory;
    private String subCategory;

    @Builder
    private ReservationGoodsResponse(Long id, Long serviceId, String serviceItemName, Integer price, Integer serviceTime,
                                    String superCategory, String baseCategory, String subCategory) {
        this.id = id;
        this.serviceId = serviceId;
        this.serviceItemName = serviceItemName;
        this.price = price;
        this.serviceTime = serviceTime;
        this.superCategory = superCategory;
        this.baseCategory = baseCategory;
        this.subCategory = subCategory;
    }

    public static ReservationGoodsResponse of(ReservationGoods reservationGoods) {
        return ReservationGoodsResponse.builder()
                .id(reservationGoods.getId())
                .serviceId(reservationGoods.getServiceId())
                .serviceItemName(reservationGoods.getServiceItemName())
                .price(reservationGoods.getPrice())
                .serviceTime(reservationGoods.getServiceTime())
                .superCategory(reservationGoods.getSuperCategory())
                .baseCategory(reservationGoods.getBaseCategory())
                .subCategory(reservationGoods.getSubCategory())
                .build();
    }
}
