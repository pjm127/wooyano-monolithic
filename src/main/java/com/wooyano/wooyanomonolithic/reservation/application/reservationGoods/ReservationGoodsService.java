package com.wooyano.wooyanomonolithic.reservation.application.reservationGoods;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.services.domain.Services;
import com.wooyano.wooyanomonolithic.services.infrastructure.ServicesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationGoodsService {

    private final ReservationGoodsRepository reservationGoodsRepository;
    private final ServicesRepository servicesRepository;


    public ReservationGoodsResponse createReservationGoods(ReservationGoodsCreateRequest request) {
        Long serviceId = request.getServiceId();
        log.info("serviceId: {}", serviceId);
        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서비스입니다."));
        ReservationGoods reservationGoods = request.toEntity(services);
        ReservationGoods saveReservationGoods = reservationGoodsRepository.save(reservationGoods);
        return ReservationGoodsResponse.of(saveReservationGoods);


    }
}
