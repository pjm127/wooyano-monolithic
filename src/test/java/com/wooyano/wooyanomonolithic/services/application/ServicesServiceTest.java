package com.wooyano.wooyanomonolithic.services.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.presentation.dto.ServiceTimeResponse;
import com.wooyano.wooyanomonolithic.services.presentation.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.services.presentation.dto.ServicesCreateResponse;
import com.wooyano.wooyanomonolithic.services.presentation.dto.ServicesResponse;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerResponse;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServicesServiceTest {

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @AfterEach
    void tearDown() {
        workerRepository.deleteAllInBatch();
        servicesRepository.deleteAllInBatch();
    }


    @DisplayName("신규 서비스를 등록한다")
    @Test
    public void createService(){
        // given
        LocalTime openTime = LocalTime.of(9, 0, 0);
        LocalTime closeTime = LocalTime.of(18, 0, 0);
        ServicesCreateRequest request = ServicesCreateRequest.builder()
                .name("테스트 서비스")
                .description("테스트 서비스입니다")
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
        // when
        ServicesCreateResponse service = servicesService.createService(request.toServiceRequest());
        // then
        assertThat(service)
                .extracting("name", "description" )
                .contains("테스트 서비스", "테스트 서비스입니다");
        ServiceTime serviceTime = service.getServiceTime();
        assertThat(serviceTime)
                .extracting("openTime", "closeTime")
                .contains(openTime, closeTime);
    }

    @DisplayName("서비스 조회시 서비스 정보, 서비스 작업시간, 작업자 정보 리스트를 함께 조회한다.")
    @Test
    public void getService(){
        // given
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
        Services save = servicesRepository.save(service);
        Worker worker1 = Worker.create("작업자1", "작업자1 폰", "작업자1 설명", service);
        Worker worker2 = Worker.create("작업자2", "작업자2 폰", "작업자2 설명", service);
        workerRepository.saveAll(List.of(worker1,worker2));
        // when
        ServicesResponse services = servicesService.getService(save.getId());
        // then
        // 1. 서비스 정보 검증
        assertThat(services.getName()).isEqualTo("서비스1");
        assertThat(services.getDescription()).isEqualTo("서비스1 설명");

        // 2. 작업자 정보 검증
        List<WorkerResponse> workers = services.getWorkers();
        assertThat(workers).hasSize(2);
        assertThat(workers.get(0).getName()).isEqualTo("작업자1");
        assertThat(workers.get(0).getPhone()).isEqualTo("작업자1 폰");
        assertThat(workers.get(0).getDescription()).isEqualTo("작업자1 설명");

        // 3. 작업시간 정보 검증
        ServiceTimeResponse serviceTime = services.getServiceTime();
        assertThat(serviceTime.getOpenTime()).isEqualTo(LocalTime.of(9, 0, 0));
        assertThat(serviceTime.getCloseTime()).isEqualTo(LocalTime.of(18, 0, 0));
    }
}