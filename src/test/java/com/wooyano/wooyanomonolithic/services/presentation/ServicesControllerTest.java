package com.wooyano.wooyanomonolithic.services.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyano.wooyanomonolithic.services.application.ServicesService;
import com.wooyano.wooyanomonolithic.services.presentation.dto.ServicesCreateRequest;
import com.wooyano.wooyanomonolithic.services.application.dto.ServicesResponse;
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

        // when // then
        mockMvc.perform(post("/api/v1/services/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("서비스 id로 서비스 정보를 조회한다")
    @Test
    public void getService() throws Exception {
        // given
        ServicesResponse response = ServicesResponse.builder()
                .name("서비스1")
                .description("서비스1 설명")
                .build();

        when(servicesService.getService(anyLong())).thenReturn(response);
        // when  // then
        mockMvc.perform(
                get("/api/v1/services/{id}", 1l)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.success").value("true"));




    }
}