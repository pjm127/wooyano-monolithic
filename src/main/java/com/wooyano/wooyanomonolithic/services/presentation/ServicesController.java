package com.wooyano.wooyanomonolithic.services.presentation;

import com.wooyano.wooyanomonolithic.services.application.ServicesService;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateResponse;
import com.wooyano.wooyanomonolithic.services.dto.ServicesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{serviceId}")
    public ServicesResponse getService(@PathVariable(name = "serviceId") Long serviceId){
        ServicesResponse service = servicesService.getService(serviceId);
        return service;
    }

}
