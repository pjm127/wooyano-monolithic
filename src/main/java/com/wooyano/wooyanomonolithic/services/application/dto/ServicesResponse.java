package com.wooyano.wooyanomonolithic.services.application.dto;

import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsResponse;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.presentation.dto.ServiceTimeResponse;
import com.wooyano.wooyanomonolithic.worker.application.dto.WorkerResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServicesResponse {
    private Long id;
    private String description;
    private String name;
    private ServiceTimeResponse serviceTime;
    private List<WorkerResponse> workers;
    private List<ReservationGoodsResponse> reservationGoods;


    @Builder
    private ServicesResponse(String description, String name, ServiceTimeResponse serviceTime, List<WorkerResponse> workers
    , List<ReservationGoodsResponse> reservationGoods) {
        this.description = description;
        this.name = name;
        this.serviceTime = serviceTime;
        this.workers = workers;
        this.reservationGoods = reservationGoods;
    }

    public static ServicesResponse of(Services services) {
        return ServicesResponse.builder()
                .description(services.getDescription())
                .name(services.getName())
                .serviceTime(ServiceTimeResponse.of(services))
                .reservationGoods(services.getReservationGoods().stream()
                        .map(reservationGoods -> ReservationGoodsResponse.of(reservationGoods))
                        .collect(Collectors.toList()))
                .workers(services.getWorkers().stream()
                        .map(worker -> WorkerResponse.of(worker))
                        .collect(Collectors.toList()))
                .build();
    }
}
