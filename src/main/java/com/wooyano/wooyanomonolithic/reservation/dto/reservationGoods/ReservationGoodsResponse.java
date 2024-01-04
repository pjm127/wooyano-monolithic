package com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationGoodsResponse {

    private Long id;
    private String baseCategory;
    private int price;
    private Long serviceId;
    private String serviceItemName;
    private String subCategory;
    private String superCategory;

    @Builder
    private ReservationGoodsResponse(Long id, String baseCategory, int price, Long serviceId, String serviceItemName,
                                     String subCategory, String superCategory) {
        this.id = id;
        this.baseCategory = baseCategory;
        this.price = price;
        this.serviceId = serviceId;
        this.serviceItemName = serviceItemName;
        this.subCategory = subCategory;
        this.superCategory = superCategory;
    }

    public static ReservationGoodsResponse of(ReservationGoods reservationGoods) {
        return ReservationGoodsResponse.builder()
                .id(reservationGoods.getId())
                .baseCategory(reservationGoods.getBaseCategory())
                .price(reservationGoods.getPrice())
                .serviceId(reservationGoods.getService().getId())
                .serviceItemName(reservationGoods.getServiceItemName())
                .subCategory(reservationGoods.getSubCategory())
                .superCategory(reservationGoods.getSuperCategory())
                .build();
    }
}
