package com.wooyano.wooyanomonolithic.webhook.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.webhook.application.dto.WebhookServiceRequest;
import com.wooyano.wooyanomonolithic.webhook.domain.ReservationHistoryWebhook;
import com.wooyano.wooyanomonolithic.webhook.infrastructure.ReservationHistoryWebhookRepository;
import com.wooyano.wooyanomonolithic.webhook.presentation.dto.DataResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationHistoryWebhookServiceTest {
    @Autowired
    private ReservationHistoryWebhookService webhookService;

    @Autowired
    private ReservationHistoryWebhookRepository reservationHistoryWebhookRepository;

    @Autowired
    private PaymentRepository paymentRepository;




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
    }

    @DisplayName("10분 간격으로 저장된 웹훅의 갯수와 결제의 갯수를 비교해서 같은 경우 true를 반환한다")
    @Test
    public void comparePaymentAndWebhookCount(){
        // given
        saveReservationHistoryWebhooks();
        savePayments();
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMinutes(10);
        // when
        boolean result = webhookService.comparePaymentAndWebhookCount(startDateTime, endDateTime);
        // then
        assertThat(result).isTrue();
    }

    @DisplayName("10분 간격으로 저장된 웹훅의 갯수와 결제의 갯수를 비교해서 다른 경우 false를 반환한다")
    @Test
    public void comparePaymentAndWebhookCountFalse(){
        // given
        saveReservationHistoryWebhooksPlus1();
        savePayments();
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        LocalDateTime endDateTime = startDateTime.plusMinutes(10);
        // when
        boolean result = webhookService.comparePaymentAndWebhookCount(startDateTime, endDateTime);
        // then
        assertThat(result).isFalse();
    }

    private void savePayments() {
        LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        Payment payment1 = Payment.createPayment("테스트 이메일", PaymentMethod.EASY_PAYMENT, 1000
                , PaymentStatus.DONE, "test order id", "test payment key",900,100,
                localDateTime);
        Payment payment2 = Payment.createPayment("테스트 이메일", PaymentMethod.EASY_PAYMENT, 1000
                , PaymentStatus.DONE, "test order id", "test payment key",900,100,
                localDateTime);
        Payment payment3 = Payment.createPayment("테스트 이메일", PaymentMethod.EASY_PAYMENT, 1000
                , PaymentStatus.CANCEL, "test order id", "test payment key",900,100,
                localDateTime);
        paymentRepository.saveAll(List.of(payment1,payment2,payment3));
    }

    private void saveReservationHistoryWebhooks() {
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        ReservationHistoryWebhook webhook1 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        ReservationHistoryWebhook webhook2 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.CANCEL);
        ReservationHistoryWebhook webhook3 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        reservationHistoryWebhookRepository.saveAll(List.of(webhook1,webhook2,webhook3));
    }

    private void saveReservationHistoryWebhooksPlus1() {
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        ReservationHistoryWebhook webhook1 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        ReservationHistoryWebhook webhook2 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.CANCEL);
        ReservationHistoryWebhook webhook3 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        ReservationHistoryWebhook webhook4 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        reservationHistoryWebhookRepository.saveAll(List.of(webhook1,webhook2,webhook3,webhook4));
    }
}