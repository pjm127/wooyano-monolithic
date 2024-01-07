package com.wooyano.wooyanomonolithic.services.application.dto;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import java.time.LocalTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

public class ServicesCreateServiceRequest {
    private String description;
    private String name;
    @DateTimeFormat(pattern = "kk:mm:ss")
    private LocalTime openTime; // ServiceTime에서 필요한 필드들을 직접 받아옴
    @DateTimeFormat(pattern = "kk:mm:ss")
    private LocalTime closeTime;

    @Builder
    private ServicesCreateServiceRequest(String description, String name, LocalTime openTime, LocalTime closeTime) {
        this.description = description;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
    public Services toEntity() {
        return Services.builder()
                .description(description)
                .name(name)
                .serviceTime(ServiceTime.builder()
                        .openTime(openTime)
                        .closeTime(closeTime)
                        .build())
                .build();
    }

}
