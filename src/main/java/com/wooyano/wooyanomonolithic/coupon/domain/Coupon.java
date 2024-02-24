package com.wooyano.wooyanomonolithic.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; // 쿠폰 이름
    private int totalQuantity; // 쿠폰의 총 수량

    @Builder
    private Coupon(String name, int totalQuantity) {
        this.name = name;
        this.totalQuantity = totalQuantity;
    }

    public void decreaseQuantity() {
        if(totalQuantity == 0) {
            throw new IllegalArgumentException("쿠폰의 수량이 없습니다.");
        }
        this.totalQuantity -=1;
    }
}
