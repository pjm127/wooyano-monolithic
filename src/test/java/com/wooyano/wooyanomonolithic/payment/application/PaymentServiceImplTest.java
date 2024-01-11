package com.wooyano.wooyanomonolithic.payment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.wooyano.wooyanomonolithic.TestContainerConfig;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentCreateRequest;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(TestContainerConfig.class)
@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WorkerTimeRepository workerTimeRepository;
    @Autowired
    private RedisService redisService;

    @DisplayName("결제 요청 전에 예약이 있는지 확인하고 예약이 없으면 결제금액과 주문번호를 임시저장한다")
    @Test
    public void savePaymentTemporarily(){
        // given
        Worker worker = getWorker();

        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .workerId(worker.getId())
                .serviceStartTime(LocalTime.of(10, 0, 0))
                .reservationDate(LocalDate.of(2024, 01, 04))
                .orderId("testOrderId")
                .paymentAmount(3000)
                .build();
        // when
        // then
        assertDoesNotThrow(() ->
                paymentService.savePaymentTemporarily(request.toServiceRequest())
        );
    }

    @DisplayName("결제 요청 전에 예약이 있는지 확인하고 예약이 있으면 예외를 반환한다")
    @Test
    public void savePaymentTemporarilyException() {
        // given
        Worker worker = getWorker();

        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .workerId(worker.getId())
                .serviceStartTime(LocalTime.of(9, 0, 0))
                .reservationDate(LocalDate.of(2024, 01, 04))
                .orderId("주문번호")
                .paymentAmount(3000)
                .build();
        // when // then
        assertThatThrownBy(() -> paymentService.savePaymentTemporarily(request.toServiceRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 예약된 서비스입니다.");
    }

    private Worker getWorker() {
        LocalTime openTime = LocalTime.of(9, 0, 0);
        LocalTime closeTime = LocalTime.of(18, 0, 0);
        ServiceTime servicesTime = ServiceTime.builder()
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
        Services service = Services.builder()
                .name("서비스1")
                .description("서비스1 설명")
                .serviceTime(servicesTime)
                .build();
        Services saveService = servicesRepository.save(service);
        Worker worker = Worker.create("작업자1", "작업자1 폰", "작업자1 설명", service);
        Worker saveWorker = workerRepository.save(worker);

        WorkerTime workerTime = WorkerTime.createWorkerTime(LocalTime.of(9, 0, 0), saveWorker,
                LocalDate.of(2024, 01, 04));
        workerTimeRepository.save(workerTime);
        return worker;
    }
}