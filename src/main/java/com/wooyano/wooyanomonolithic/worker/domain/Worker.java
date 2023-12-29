package com.wooyano.wooyanomonolithic.worker.domain;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "worker")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20, name = "name")
    private String name;

    @Column(nullable = false, length = 20, name = "phone")
    private String phone;

    @Column(nullable = false, length = 255, name = "description")
    private String description;





    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Services service;


    @Builder
    private Worker(String name, String phone, String description, Services service) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.service = service;
    }
}