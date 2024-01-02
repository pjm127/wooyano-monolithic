package com.wooyano.wooyanomonolithic.services.application;

import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateResponse;
import com.wooyano.wooyanomonolithic.services.dto.ServicesResponse;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServiceTimeRepository;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class ServicesService {

    private final ServicesRepository servicesRepository;

    private final ServiceTimeRepository serviceTimeRepository;

    @Transactional
    public ServicesCreateResponse createService(ServicesCreateRequest request) {
        Services services = request.toEntity();
        Services save = servicesRepository.save(services);
        return ServicesCreateResponse.of(save);
    }


    //서비스 조회시 서비스 정보 작업자 정보 리스트
    public ServicesResponse getService(Long serviceId) {
        Services services = servicesRepository.findByIdWithWorkersAndServiceTime(serviceId);
        return ServicesResponse.of(services);
    }
}
