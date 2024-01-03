package com.wooyano.wooyanomonolithic.worker.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class WorkerTimeRepositoryTest {

    @Autowired
    private WorkerTimeRepository workerTimeRepository;
    @Autowired
    private ServicesRepository servicesRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @DisplayName("해당하는 작업자가 예약하고 싶은 날짜의 시간에 작업이 있으면 WorkTime을 반환한다")
    @Test
    public void findByWorkerAndServiceTime(){
        // given
        Worker saveWorker = getWorker();
        LocalDate reservationDate = LocalDate.of(2024, 01, 03);
        LocalTime serviceStart = LocalTime.of(10, 0, 0);
        WorkerTime saveWorkerTime = WorkerTime.createWorkerTime(serviceStart,saveWorker, reservationDate);
        WorkerTime workerTime = workerTimeRepository.save(saveWorkerTime);
        // when
        Optional<WorkerTime> byWorkerAndServiceTime = workerTimeRepository.findByWorkerAndServiceTime(saveWorker,
                serviceStart, reservationDate);
        // then
        assertThat(byWorkerAndServiceTime.get().getId()).isEqualTo(workerTime.getId());
    }

    @DisplayName("해당하는 작업자가 예약하고 싶은 날짜의 시간에 작업이 없으면 NULL을 반환한다")
    @Test
    public void 테스트이름(){
        // given

        // when

        // then
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
        return saveWorker;
    }


}