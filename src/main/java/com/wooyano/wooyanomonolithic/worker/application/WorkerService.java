package com.wooyano.wooyanomonolithic.worker.application;

import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.NOT_FOUND_SERVICE;

import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import com.wooyano.wooyanomonolithic.worker.application.dto.WorkerCreateServiceRequest;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.presentation.dto.WorkerCreateRequest;
import com.wooyano.wooyanomonolithic.worker.application.dto.WorkerResponse;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class WorkerService {
    
    private final WorkerRepository workerRepository;

    private final ServicesRepository servicesRepository;


    @Transactional
    public WorkerResponse createWorker(WorkerCreateServiceRequest request ) {
        Long serviceId = request.getServiceId();
        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SERVICE));
        Worker worker = request.toEntity(services);
        Worker save = workerRepository.save(worker);
        return WorkerResponse.of(save);
    }


    public List<WorkerResponse> getWorkerList(Long serviceId) {
        List<Worker> workers = workerRepository.findByServiceId(serviceId);
        return workers.stream()
                .map(worker -> WorkerResponse.of(worker))
                .collect(Collectors.toList());
    }
}
