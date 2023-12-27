package com.wooyano.wooyanomonolithic.reservation.domain;

import com.wooyano.wooyanomonolithic.global.common.domain.BaseEntity;
import com.wooyano.wooyanomonolithic.service.domain.Services;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ReservationGoods extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;*/
/*    @Column(nullable = false, name = "service_id")
    private Long serviceId;*/
    @Column(nullable = false, length = 30, name = "service_item_name")
    private String serviceItemName;
    @Column(nullable = false, name = "price")
    private Integer price;
    @Column(nullable = false, name = "service_time")
    private Integer serviceTime;
    @Column(nullable = false, length = 20, name = "super_category")
    private String superCategory;
    @Column(nullable = false, length = 20, name = "base_category")
    private String baseCategory;
    @Column(length = 20, name = "sub_category")
    private String subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Services service;

    @Builder
    public ReservationGoods(String serviceItemName, Integer price, Integer serviceTime, String superCategory,
                            String baseCategory, String subCategory, Services service) {
        this.serviceItemName = serviceItemName;
        this.price = price;
        this.serviceTime = serviceTime;
        this.superCategory = superCategory;
        this.baseCategory = baseCategory;
        this.subCategory = subCategory;
        this.service = service;
    }

    public static ReservationGoods createReservationGoods(String serviceItemName, Integer price, Integer serviceTime,
                                          String superCategory, String baseCategory, String subCategory,Services service) {
        return ReservationGoods.builder()
                .serviceItemName(serviceItemName)
                .price(price)
                .serviceTime(serviceTime)
                .superCategory(superCategory)
                .baseCategory(baseCategory)
                .subCategory(subCategory)
                .service(service)
                .build();
    }





}
