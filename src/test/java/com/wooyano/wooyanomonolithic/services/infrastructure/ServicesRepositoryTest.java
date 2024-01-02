package com.wooyano.wooyanomonolithic.services.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerCreateRequest;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServicesRepositoryTest {

    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @AfterEach
    void tearDown() {
        workerRepository.deleteAllInBatch();
        servicesRepository.deleteAllInBatch();
    }

    @DisplayName("서비스 id로 서비스 정보와 해당하는 작업시간 및 작업자을 가져온다")
    @Test
    public void findByIdWithWorkersAndServiceTime(){
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
        Services services = servicesRepository.findByIdWithWorkersAndServiceTime(save.getId());
        // then
        // 1. 서비스 정보 검증
        assertThat(services.getId()).isEqualTo(save.getId());
        assertThat(services.getName()).isEqualTo("서비스1");
        assertThat(services.getDescription()).isEqualTo("서비스1 설명");

        // 2. 작업자 정보 검증
        List<Worker> workers = services.getWorkers();
        assertThat(workers).hasSize(2);
        assertThat(workers.get(0).getName()).isEqualTo("작업자1");
        assertThat(workers.get(0).getPhone()).isEqualTo("작업자1 폰");
        assertThat(workers.get(0).getDescription()).isEqualTo("작업자1 설명");

        // 3. 작업시간 정보 검증
        ServiceTime serviceTime = services.getServiceTime();
        assertThat(serviceTime).isNotNull();
        assertThat(serviceTime.getOpenTime()).isEqualTo(LocalTime.of(9, 0, 0));
        assertThat(serviceTime.getCloseTime()).isEqualTo(LocalTime.of(18, 0, 0));

    }
}