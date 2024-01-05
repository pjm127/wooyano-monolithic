package com.wooyano.wooyanomonolithic.services.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyano.wooyanomonolithic.services.application.ServicesService;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.services.dto.ServicesCreateResponse;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ServicesController.class)
class ServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ServicesService servicesService;

    @DisplayName("신규 서비스를 등록한다")
    @Test
    public void createServices() throws Exception {
        // given
        ServicesCreateRequest request = ServicesCreateRequest.builder()
                .name("서비스1")
                .description("서비스1 설명")
                .openTime(LocalTime.of(9, 0, 0))
                .closeTime(LocalTime.of(18, 0, 0))
                .build();

        ServicesCreateResponse mockResponse = ServicesCreateResponse.builder()
                .id(1L)
                .name("서비스1")
                .description("서비스1 설명")
                .build();

        when(servicesService.createService(any())).thenReturn(mockResponse);

        // when // then
        mockMvc.perform(post("/api/v1/services/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.name").value("서비스1"))
                .andExpect(jsonPath("$.result.description").value("서비스1 설명"));
    }
}