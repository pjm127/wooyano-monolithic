package com.wooyano.wooyanomonolithic.service.presentation;

import com.wooyano.wooyanomonolithic.service.application.ServicesService;
import com.wooyano.wooyanomonolithic.service.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.service.dto.ServicesCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class ServicesController {

    private final ServicesService servicesService;

    @PostMapping("/new")
    public ServicesCreateResponse createService(@RequestBody ServicesCreateRequest request){
        ServicesCreateResponse service = servicesService.createService(request);
        return service;
    }

}
