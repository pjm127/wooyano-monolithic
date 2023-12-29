package com.wooyano.wooyanomonolithic.service.dto;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServicesResponse {
    private Long id;
    private String description;
    private String name;
    private ServiceTimeResponse serviceTime;
    private List<WorkerResponse> workers;


    @Builder
    private ServicesResponse(String description, String name, ServiceTimeResponse serviceTime, List<WorkerResponse> workers) {
        this.description = description;
        this.name = name;
        this.serviceTime = serviceTime;
        this.workers = workers;
    }

    public static ServicesResponse of(Services services) {
        return ServicesResponse.builder()
                .description(services.getDescription())
                .name(services.getName())
                .serviceTime(ServiceTimeResponse.of(services))
                .workers(services.getWorkers().stream()
                        .map(worker -> WorkerResponse.of(worker))
                        .collect(Collectors.toList()))
                .build();
    }
}
