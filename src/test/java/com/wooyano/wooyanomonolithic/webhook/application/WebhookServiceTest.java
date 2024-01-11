package com.wooyano.wooyanomonolithic.webhook.application;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
import com.wooyano.wooyanomonolithic.webhook.presentation.dto.DataResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebhookServiceTest {
    @Mock
    private ReservationHistoryWebhookService webhookService;

    @DisplayName("결제 상태 변경 시 webhook 받아 저장한다 ")
    @Test
    public void saveWebhook(){
        // given
        String stringCreateAt = "2022-01-01T00:00:00.000000";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(stringCreateAt, formatter);
        DataResponse data = DataResponse.builder()
                .orderId("test1159")
                .status("DONE")
                .build();
        WebhookServiceRequest request = WebhookServiceRequest.builder()
                .createdAt(localDateTime)
                .data(data)
                .build();
        // when
        webhookService.saveWebhook(request);
        // then
        verify(webhookService, times(1)).saveWebhook(any());
    }
}