package com.wooyano.wooyanomonolithic.worker.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@DataJpaTest
class WorkerRepositoryTest {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    @DisplayName("서비스id로 해당하는 작업자의 리스트를 조회한다")
    @Test
    public void findByServiceId(){
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
        Services saveService = servicesRepository.save(service);
        Worker worker1 = Worker.create("작업자1", "작업자1 폰", "작업자1 설명", service);
        Worker worker2 = Worker.create("작업자2", "작업자2 폰", "작업자2 설명", service);
        workerRepository.saveAll(List.of(worker1,worker2));

        // when
        List<Worker> workers = workerRepository.findByServiceId(saveService.getId());
        // then
        assertThat(workers).hasSize(2)
                .extracting("name", "phone", "description")
                .containsExactlyInAnyOrder(
                        tuple("작업자1", "작업자1 폰", "작업자1 설명"),
                        tuple("작업자2", "작업자2 폰", "작업자2 설명")
                );
    }

}