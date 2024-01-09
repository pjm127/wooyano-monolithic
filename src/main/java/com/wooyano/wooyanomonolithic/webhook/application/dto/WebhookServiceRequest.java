package com.wooyano.wooyanomonolithic.webhook.application.dto;

import com.wooyano.wooyanomonolithic.webhook.presentation.dto.DataResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class WebhookServiceRequest {
    private String createdAt;
    private DataResponse data;

    @Builder
    private WebhookServiceRequest(String createdAt, DataResponse data) {
        this.createdAt = createdAt;
        this.data = data;
    }
}
