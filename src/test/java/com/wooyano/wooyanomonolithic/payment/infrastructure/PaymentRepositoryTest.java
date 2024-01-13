package com.wooyano.wooyanomonolithic.payment.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @DisplayName("order id로 payment 정보를 가져온다")
    @Test
    public void findByOrderId(){
        // given
        LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 1, 0, 0, 0);
        Payment payment = Payment.createPayment("테스트 이메일", PaymentMethod.EASY_PAYMENT, 1000
                , PaymentStatus.DONE, "test order id", "test payment key",900,100,
                localDateTime);
        Payment save = paymentRepository.save(payment);
        // when
        Payment savePayment = paymentRepository.findByOrderId(payment.getOrderId());
        // then
        assertThat(savePayment.getOrderId()).isEqualTo(save.getOrderId());
    }

    @DisplayName("10분 간격으로 저장된 웹훅의 갯수를 count한다")
    @Test
    public void countByApprovedAtBetweenAndStatus(){
        // given
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
        // when
        long count = paymentRepository.countByApprovedAtBetweenAndStatus(localDateTime, localDateTime.plusMinutes(10),
                PaymentStatus.DONE);
        // then
        assertThat(count).isEqualTo(2);
    }

}