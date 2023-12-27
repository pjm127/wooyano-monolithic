package com.wooyano.wooyanomonolithic.worker.presentation;

import com.wooyano.wooyanomonolithic.worker.application.WorkerService;
import com.wooyano.wooyanomonolithic.worker.dto.WorkerCreateRequest;
import lombok.RequiredArgsConstructor;
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
    public void createWorker(@RequestBody WorkerCreateRequest request) {
        workerService.createWorker(request);
    }
}

