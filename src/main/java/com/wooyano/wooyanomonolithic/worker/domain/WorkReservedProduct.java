package com.wooyano.wooyanomonolithic.worker.domain;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class WorkReservedProduct {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Worker worker;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReservationGoods reservationGoods;

}
