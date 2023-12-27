package com.wooyano.wooyanomonolithic.worker.dto;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkerCreateRequest {

    private String name;
    private String phone;
    private String description;
    private Long serviceId;
    @Builder
    private WorkerCreateRequest(String name, String phone, String description, Long serviceId) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.serviceId = serviceId;
    }

    public Worker toEntity(Services services) {
        return Worker.builder()
                .name(name)
                .phone(phone)
                .description(description)
                .service(services)
                .build();
    }
}
