package com.wooyano.wooyanomonolithic.webhook.presentation.dto;

import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebhookRequest {
    private String createdAt;
    private DataResponse data;

    @Builder
    private WebhookRequest(String createdAt, DataResponse data) {
        this.createdAt = createdAt;
        this.data = data;
    }

    public WebhookServiceRequest toServiceRequest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime createdAtDateTime = LocalDateTime.parse(createdAt, formatter);

        return WebhookServiceRequest.builder()
            .createdAt(createdAtDateTime)
            .data(data)
            .build();
    }
}
