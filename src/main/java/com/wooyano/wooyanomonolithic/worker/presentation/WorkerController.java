package com.wooyano.wooyanomonolithic.worker.presentation;

import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.worker.application.WorkerService;
import com.wooyano.wooyanomonolithic.worker.presentation.dto.WorkerCreateRequest;
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
    public BaseResponse<?> createWorker(@RequestBody WorkerCreateRequest request) {
        return new BaseResponse<>(workerService.createWorker(request.toServiceRequest()));
    }

    @GetMapping("/list/{serviceId}")
    public BaseResponse<?> getWorkerList(@PathVariable Long serviceId) {
        return new BaseResponse<>(workerService.getWorkerList(serviceId));
    }
}

