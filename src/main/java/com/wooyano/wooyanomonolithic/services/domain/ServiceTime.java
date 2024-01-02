package com.wooyano.wooyanomonolithic.services.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime openTime;
    private LocalTime closeTime;
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "serviceTime")
    private Services service;

    //9 10 11 12 13 14 15 16 17 18 19 20 21 22

    @Builder
    public ServiceTime(LocalTime openTime, LocalTime closeTime, Services service) {
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.service = service;

    }

}
