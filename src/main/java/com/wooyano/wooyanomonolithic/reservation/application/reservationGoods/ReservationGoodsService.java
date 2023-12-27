package com.wooyano.wooyanomonolithic.reservation.application.reservationGoods;

import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationGoodsService {

    private final ReservationGoodsRepository reservationGoodsRepository;


    public void createReservationGoods(ReservationGoodsCreateRequest request) {
        ReservationGoods save = reservationGoodsRepository.save(request.toEntity());

    }
}
