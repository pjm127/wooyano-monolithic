package com.wooyano.wooyanomonolithic.worker.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.presentation.dto.WorkerCreateRequest;
import com.wooyano.wooyanomonolithic.worker.application.dto.WorkerResponse;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorkerServiceTest {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private ServicesRepository servicesRepository;

    @AfterEach
    void tearDown() {
        servicesRepository.deleteAllInBatch();
    }

    @DisplayName("신규 작업자를 등록한다")
    @Test
    public void createWorker(){
        // given
        Services services = getServices();
        WorkerCreateRequest request = WorkerCreateRequest.builder()
                .name("작업자1")
                .phone("작업자1 폰")
                .description("작업자1 설명")
                .serviceId(services.getId())
                .build();

        // when
        WorkerResponse worker = workerService.createWorker(request);
        // then
        assertThat(worker)
                .extracting("name", "phone", "description")
                .contains("작업자1", "작업자1 폰", "작업자1 설명");
    }

    @DisplayName("신규 작업자를 등록하는데 해당하는 서비스가 없으면 예외가 발생한다")
    @Test
    public void createWorkerWithNonExistingService(){
        // given
        Services services = getServices();
        WorkerCreateRequest request = WorkerCreateRequest.builder()
                .name("작업자1")
                .phone("작업자1 폰")
                .description("작업자1 설명")
                .serviceId(2l)
                .build();
        // when

        // then
        assertThatThrownBy(() -> workerService.createWorker(request.toServiceRequest()))
                .isInstanceOf(CustomException.class)
                .hasMessage("서비스가 존재하지 않습니다.");
    }

    private Services getServices() {
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
        return servicesRepository.save(service);
    }
}