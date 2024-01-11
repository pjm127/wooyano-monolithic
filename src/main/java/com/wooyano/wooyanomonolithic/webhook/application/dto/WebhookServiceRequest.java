package com.wooyano.wooyanomonolithic.webhook.application.dto;

import com.wooyano.wooyanomonolithic.webhook.presentation.dto.DataResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class WebhookServiceRequest {
    private LocalDateTime createdAt;
    private DataResponse data;

    @Builder
    private WebhookServiceRequest(LocalDateTime createdAt, DataResponse data) {
        this.createdAt = createdAt;
        this.data = data;
    }
}
