package com.wooyano.wooyanomonolithic.webhook.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataResponse {
    private String orderId;
    private String status;

    @Builder
    private DataResponse(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }
}
