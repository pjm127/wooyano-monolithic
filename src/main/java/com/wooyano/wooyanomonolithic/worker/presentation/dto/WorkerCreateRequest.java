package com.wooyano.wooyanomonolithic.worker.presentation.dto;

import com.wooyano.wooyanomonolithic.services.application.dto.ServicesCreateServiceRequest;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.worker.application.dto.WorkerCreateServiceRequest;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
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



    public WorkerCreateServiceRequest toServiceRequest(){
        return WorkerCreateServiceRequest.builder()
                .name(name)
                .phone(phone)
                .description(description)
                .serviceId(serviceId)
                .build();
    }
}
