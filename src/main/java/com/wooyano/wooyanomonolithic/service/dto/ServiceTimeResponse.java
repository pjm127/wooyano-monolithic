package com.wooyano.wooyanomonolithic.service.dto;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServiceTimeResponse {


    private LocalTime openTime;
    private LocalTime closeTime;


    @Builder
    private ServiceTimeResponse(LocalTime openTime, LocalTime closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;

    }

    public static ServiceTimeResponse of(Services services) {
        return ServiceTimeResponse.builder()
                .openTime(services.getServiceTime().getOpenTime())
                .closeTime(services.getServiceTime().getCloseTime())

                .build();
    }
}
