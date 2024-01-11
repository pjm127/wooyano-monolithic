package com.wooyano.wooyanomonolithic.webhook.presentation;

import com.wooyano.wooyanomonolithic.webhook.application.ReservationHistoryWebhookService;
import com.wooyano.wooyanomonolithic.webhook.presentation.dto.WebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhook")
public class ReservationHistoryWebhookController {
    private final ReservationHistoryWebhookService webhookService;
    @PostMapping("/test")
    public void test(@RequestBody WebhookRequest webhookRequest) {
        webhookService.saveWebhook(webhookRequest.toServiceRequest());
    }

}
