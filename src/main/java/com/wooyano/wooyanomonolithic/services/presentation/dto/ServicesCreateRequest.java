package com.wooyano.wooyanomonolithic.services.presentation.dto;

import com.wooyano.wooyanomonolithic.services.application.dto.ServicesCreateServiceRequest;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class ServicesCreateRequest {
    private String description;
    private String name;
    @DateTimeFormat(pattern = "kk:mm:ss")
    private LocalTime openTime;
    @DateTimeFormat(pattern = "kk:mm:ss")
    private LocalTime closeTime;

    @Builder
    private ServicesCreateRequest(String description, String name, LocalTime openTime, LocalTime closeTime) {
        this.description = description;
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }



    public ServicesCreateServiceRequest toServiceRequest(){
        return ServicesCreateServiceRequest.builder()
                .description(description)
                .name(name)
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
    }
}
