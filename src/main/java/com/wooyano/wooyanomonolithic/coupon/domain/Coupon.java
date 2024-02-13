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
    private String code; // 쿠폰 코드
    private double discountRate; // 할인율 (예: 0.1은 10% 할인)
    private double discountAmount; // 정확한 금액의 할인 (예: 1000은 1000원 할인)
    private boolean used; // 쿠폰 사용 여부
    private LocalDate expirationDate;  //쿠폰 만료일 = 쿠폰 발급 날짜 당일
    private int totalQuantity; // 쿠폰의 총 수량

}
