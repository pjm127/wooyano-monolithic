package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wooyano.wooyanomonolithic.TestContainerConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReservationServiceImplTest {

    @Autowired
    private ReservationServiceImpl reservationService;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WorkerTimeRepository workerTimeRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @AfterEach
    void tearDown() {
        reservationRepository.deleteAllInBatch();
        workerTimeRepository.deleteAllInBatch();
        workerRepository.deleteAllInBatch();
        servicesRepository.deleteAllInBatch();
    }

    @DisplayName("해당 작업자의 예약 가능 여부를 확인한다")
    @Test
    public void checkWorkerAvailabilityWhenAvailable() {
        // given
        Worker worker = getWorker();
        LocalDate reservationDate = LocalDate.now();
        LocalTime serviceStart = LocalTime.of(10, 0, 0);

        // when, then
        assertDoesNotThrow(() ->
                reservationService.checkWorkerAvailability(worker, reservationDate, serviceStart)
        );
    }


    @DisplayName("해당 작업자의 예약 가능 여부를 확인하는데 이미 해당 작업자가 예약된 시간이 있으면 예외를 던진다")
    @Test
    public void checkWorkerAvailabilityWhenNotAvailable() {
        // given
        Worker worker = getWorker();
        LocalDate reservationDate = LocalDate.now();
        LocalTime serviceStart = LocalTime.of(9, 0,0);

        // when, then

        assertThatThrownBy(() ->
                reservationService.checkWorkerAvailability(worker, reservationDate, serviceStart)
        )
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이미 예약된 서비스입니다.");

    }

    @DisplayName("작업자의 작업일정, 예약 정보, 결제 정보를 저장한다")
    @Test
    public void saveWorkTimeAndReservationAndPayment(){
        // given

        String paymentKey = "paymentKey";
        String orderId = "testOrderId2";
        int amount = 10000;
        Long serviceId = 3L;
        Long workerId = 2L;
        String userEmail = "user@example.com";
        LocalDate reservationDate = LocalDate.of(2024, 01, 04);
        String request = "request";
        String address = "address";
        String clientEmail = "client@example.com";
        LocalTime serviceStart = LocalTime.of(10, 0, 0);
        List<Long> reservationGoodsId = List.of(1L, 2L);
        int suppliedAmount = 8000;
        int vat = 2000;
        String status = "DONE";
        String method = "간편결제";
        String approvedAt = "2022-01-01T00:00:00+09:00";
        Worker worker = getWorker();
        // when
        ReservationResponse reservationResponse = reservationService.saveWorkTimeAndReservationAndPayment(paymentKey,
                orderId, amount, serviceId, workerId,
                userEmail, reservationDate, request, address, clientEmail, serviceStart, reservationGoodsId,
                suppliedAmount, vat, status, method, worker,approvedAt);
        // then
        assertThat(reservationResponse)
                .extracting("orderId", "amount", "reservationDate", "request", "address", "serviceStart")
                .contains("testOrderId2", 10000, LocalDate.of(2024, 01, 04), "request", "address", LocalTime.of(10, 0, 0));
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
        Worker worker = Worker.create("작업자1", "작업자1 폰", "작업자1 설명", saveService);
        Worker saveWorker = workerRepository.save(worker);

        WorkerTime workerTime = WorkerTime.createWorkerTime(LocalTime.of(9, 0, 0), saveWorker,
                LocalDate.now());
        workerTimeRepository.save(workerTime);
        return worker;
    }
}