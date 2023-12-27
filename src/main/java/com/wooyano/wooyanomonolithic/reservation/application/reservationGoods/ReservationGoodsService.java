package com.wooyano.wooyanomonolithic.reservation.application.reservationGoods;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.service.domain.Services;
import com.wooyano.wooyanomonolithic.service.infrastructure.ServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationGoodsService {

    private final ReservationGoodsRepository reservationGoodsRepository;
    private final ServicesRepository servicesRepository;


    public void createReservationGoods(ReservationGoodsCreateRequest request) {
        Long serviceId = request.getServiceId();
        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서비스입니다."));
        ReservationGoods reservationGoods = request.toEntity(services);
        reservationGoodsRepository.save(reservationGoods);


    }
}
