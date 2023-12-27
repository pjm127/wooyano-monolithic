package com.wooyano.wooyanomonolithic.service.dto;

import com.wooyano.wooyanomonolithic.service.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.service.domain.Services;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServicesCreateResponse {
    private Long id;
    private String description;
    private String name;
    private ServiceTime serviceTime;

    @Builder
    private ServicesCreateResponse(Long id,String description, String name, ServiceTime serviceTime) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.serviceTime = serviceTime;
    }

    public static ServicesCreateResponse of(Services services) {
        return ServicesCreateResponse.builder()
                .id(services.getId())
                .description(services.getDescription())
                .name(services.getName())
                .serviceTime(services.getServiceTime())
                .build();
    }
}
