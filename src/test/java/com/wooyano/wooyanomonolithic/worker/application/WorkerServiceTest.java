package com.wooyano.wooyanomonolithic.worker.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerCreateRequest;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerResponse;
import java.time.LocalTime;
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