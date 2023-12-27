package com.wooyano.wooyanomonolithic.worker.dto;

import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import jakarta.persistence.Column;
import lombok.Builder;

public class WorkerResponse {

    private String name;
    private String phone;
    private String description;

    @Builder
    private WorkerResponse(String name, String phone, String description) {
        this.name = name;
        this.phone = phone;
        this.description = description;
    }

    public static WorkerResponse of(Worker worker) {
        return WorkerResponse.builder()
                .name(worker.getName())
                .phone(worker.getPhone())
                .description(worker.getDescription())
                .build();
    }
}
