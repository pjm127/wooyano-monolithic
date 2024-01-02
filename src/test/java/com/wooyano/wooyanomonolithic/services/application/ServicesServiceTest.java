package com.wooyano.wooyanomonolithic.services.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.wooyano.wooyanomonolithic.services.domain.ServiceTime;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateResponse;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServicesServiceTest {

    @Autowired
    private ServicesService servicesService;

    @DisplayName("신규 서비스를 등록한다")
    @Test
    public void createService(){
        // given
        LocalTime openTime = LocalTime.of(9, 0, 0);
        LocalTime closeTime = LocalTime.of(18, 0, 0);
        ServicesCreateRequest request = ServicesCreateRequest.builder()
                .name("테스트 서비스")
                .description("테스트 서비스입니다")
                .openTime(openTime)
                .closeTime(closeTime)
                .build();
        // when
        ServicesCreateResponse service = servicesService.createService(request);
        // then
        assertThat(service)
                .extracting("name", "description" )
                .contains("테스트 서비스", "테스트 서비스입니다");
        ServiceTime serviceTime = service.getServiceTime();
        assertThat(serviceTime)
                .extracting("openTime", "closeTime")
                .contains(openTime, closeTime);
    }

}