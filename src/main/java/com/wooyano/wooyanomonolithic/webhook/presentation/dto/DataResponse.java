package com.wooyano.wooyanomonolithic.webhook.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DataResponse {
    private String orderId;
    private String status;
}
