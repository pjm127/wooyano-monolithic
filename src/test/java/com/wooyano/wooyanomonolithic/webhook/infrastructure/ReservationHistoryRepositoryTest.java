package com.wooyano.wooyanomonolithic.webhook.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.webhook.domain.ReservationHistoryWebhook;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationHistoryRepositoryTest {

    @Autowired
    private ReservationHistoryRepository reservationHistoryRepository;

    @DisplayName("10분 간격으로 저장된 웹훅의 갯수를 count한다")
    @Test
    public void countByApprovedAtBetweenAndStatus(){
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        ReservationHistoryWebhook webhook1 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        ReservationHistoryWebhook webhook2 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.CANCEL);
        ReservationHistoryWebhook webhook3 = ReservationHistoryWebhook.create("testOrderId2", startDateTime,
                PaymentStatus.DONE);
        reservationHistoryRepository.saveAll(List.of(webhook1,webhook2,webhook3));
        // when
        long count = reservationHistoryRepository.countByApprovedAtBetweenAndStatus(startDateTime, startDateTime.plusMinutes(10),
                PaymentStatus.DONE);
        // then
        assertThat(count).isEqualTo(2);
    }

}