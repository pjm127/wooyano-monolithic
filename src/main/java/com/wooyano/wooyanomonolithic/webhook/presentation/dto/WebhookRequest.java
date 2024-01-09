package com.wooyano.wooyanomonolithic.webhook.presentation.dto;

import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
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
        return WebhookServiceRequest.builder()
            .createdAt(createdAt)
            .data(data)
            .build();
    }
}
