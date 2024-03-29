package com.wooyano.wooyanomonolithic.worker.application.dto;

import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WorkerResponse {

    private Long id;
    private String name;
    private String phone;
    private String description;

    @Builder
    private WorkerResponse(Long id,String name, String phone, String description) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
    }

    public static WorkerResponse of(Worker worker) {
        return WorkerResponse.builder()
                .id(worker.getId())
                .name(worker.getName())
                .phone(worker.getPhone())
                .description(worker.getDescription())
                .build();
    }
}
