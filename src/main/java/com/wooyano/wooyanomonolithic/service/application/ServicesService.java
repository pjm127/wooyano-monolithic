package com.wooyano.wooyanomonolithic.service.application;

import com.wooyano.wooyanomonolithic.service.domain.Services;
import com.wooyano.wooyanomonolithic.service.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.service.dto.ServicesCreateResponse;
import com.wooyano.wooyanomonolithic.service.infrastructure.ServiceTimeRepository;
import com.wooyano.wooyanomonolithic.service.infrastructure.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServicesService {

    private final ServicesRepository servicesRepository;

    private final ServiceTimeRepository serviceTimeRepository;

    public ServicesCreateResponse createService(ServicesCreateRequest request) {
        Services services = request.toEntity();
        Services save = servicesRepository.save(services);
        return ServicesCreateResponse.of(save);
    }
}
