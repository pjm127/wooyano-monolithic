package com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationGoodsCreateRequest {
    private String baseCategory;
    private int price;
    private Long serviceId;
    private String serviceItemName;
    private int serviceTime;
    private String subCategory;
    private String superCategory;

    @Builder
    private ReservationGoodsCreateRequest(String baseCategory, int price, Long serviceId, String serviceItemName,
                                         int serviceTime, String subCategory, String superCategory) {
        this.baseCategory = baseCategory;
        this.price = price;
        this.serviceId = serviceId;
        this.serviceItemName = serviceItemName;
        this.serviceTime = serviceTime;
        this.subCategory = subCategory;
        this.superCategory = superCategory;
    }

    public ReservationGoods toEntity() {
        return ReservationGoods.builder()
                .baseCategory(baseCategory)
                .price(price)
                .serviceId(serviceId)
                .serviceItemName(serviceItemName)
                .serviceTime(serviceTime)
                .subCategory(subCategory)
                .superCategory(superCategory)
                .build();
    }
}
