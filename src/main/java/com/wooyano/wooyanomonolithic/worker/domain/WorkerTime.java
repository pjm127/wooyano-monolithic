package com.wooyano.wooyanomonolithic.worker.domain;

import com.wooyano.wooyanomonolithic.global.common.domain.BaseEntity;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerTime  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime serviceTime;

    private LocalDate registeredDate;

 /*   @Version
    private Integer version;*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @Builder
    private WorkerTime( LocalTime serviceTime, Worker worker, LocalDate registeredDate) {
        this.serviceTime = serviceTime;
        this.worker = worker;
        this.registeredDate = registeredDate;
    }

    public static WorkerTime createWorkerTime(LocalTime serviceTime, Worker worker, LocalDate registeredDate) {
        return WorkerTime.builder()
                .serviceTime(serviceTime)
                .worker(worker)
                .registeredDate(registeredDate)
                .build();
    }
}
