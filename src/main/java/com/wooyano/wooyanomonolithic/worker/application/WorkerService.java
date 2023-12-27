package com.wooyano.wooyanomonolithic.worker.application;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import com.wooyano.wooyanomonolithic.service.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerCreateRequest;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerResponse;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WorkerService {
    
    private final WorkerRepository workerRepository;

    private final ServicesRepository servicesRepository;


    public void createWorker(WorkerCreateRequest request ) {
        Long serviceId = request.getServiceId();
        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서비스입니다."));

        Worker worker = request.toEntity(services);
        workerRepository.save(worker);
    }


    public List<WorkerResponse> getWorkerList(Long serviceId) {
        List<Worker> workers = workerRepository.findByServiceId(serviceId);
        return workers.stream()
                .map(worker -> WorkerResponse.of(worker))
                .collect(Collectors.toList());
    }
}
