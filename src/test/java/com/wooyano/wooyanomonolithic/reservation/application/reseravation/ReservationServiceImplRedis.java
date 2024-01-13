package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.wooyano.wooyanomonolithic.TestContainerConfig;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(TestContainerConfig.class)
@SpringBootTest
class ReservationServiceImplRedis {

    @Autowired
    private RedisService redisService;
    @Autowired
    private ReservationService reservationService;

    @DisplayName("orderId와 amount를 redis에 저장된 것과 비교한다.")
    @Test
    public void verifyPayment(){
        // given
        String orderId = "testOrder12";
        int amount = 10000;
        redisService.setValues(orderId, String.valueOf(amount), Duration.ofMinutes(30));
        // when
        reservationService.verifyPayment(orderId, amount);
        // then
    }
    @DisplayName("orderId와 amount를 redis에 저장된 것과 비교한다. 금액이 일치하지 않는 경우 예외를 발생시킨다.")
    @Test
    public void verifyPaymentAmountMismatch (){
        // given
        String orderId = "testOrder12";
        int amount = 10000;
        redisService.setValues(orderId, String.valueOf(amount), Duration.ofMinutes(30));
        // when // then
        assertThatThrownBy(()-> reservationService.verifyPayment(orderId, amount+1000))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("결제 금액이 일치하지 않습니다.");

    }

}