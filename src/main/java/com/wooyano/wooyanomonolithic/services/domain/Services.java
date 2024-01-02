package com.wooyano.wooyanomonolithic.services.domain;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "service")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 500,name = "description")
    private String description;

    @Column(nullable = false,length = 30,name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "service_time_id")
    private ServiceTime serviceTime;

    @OneToMany(mappedBy = "service",cascade = CascadeType.ALL)
    private List<Worker> workers = new ArrayList<>();

    @OneToMany(mappedBy = "service",cascade = CascadeType.ALL)
    private List<ReservationGoods> reservationGoods = new ArrayList<>();


    @Builder
    private Services(String description, String name, ServiceTime serviceTime) {
        this.description = description;
        this.name = name;
        this.serviceTime = serviceTime;
    }


}