package com.wooyano.wooyanomonolithic.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
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
    private boolean used; // 쿠폰 사용 여부
    private int totalQuantity; // 쿠폰의 총 수량

    private int remainingQuantity; // 쿠폰의 남은 수량



    public void decreaseQuantity() {
        if(remainingQuantity == 0) {
            throw new IllegalArgumentException("쿠폰의 수량이 없습니다.");
        }
        this.remainingQuantity -=1;
    }
}
