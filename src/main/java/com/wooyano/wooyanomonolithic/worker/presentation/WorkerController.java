package com.wooyano.wooyanomonolithic.worker.presentation;

import com.wooyano.wooyanomonolithic.worker.application.WorkerService;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerCreateRequest;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerResponse;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping("/new")
    public WorkerResponse createWorker(@RequestBody WorkerCreateRequest request) {
        return workerService.createWorker(request);
    }

    @GetMapping("/list/{serviceId}")
    public List<WorkerResponse> getWorkerList(@PathVariable Long serviceId) {
        return workerService.getWorkerList(serviceId);
    }
}

