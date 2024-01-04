package com.wooyano.wooyanomonolithic.reservation.presentation;

import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.reservation.application.reservationGoods.ReservationGoodsService;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservationGoods.ReservationGoodsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservationGoods")
public class ReservationGoodsController {

    private final ReservationGoodsService reservationGoodsService;


    @PostMapping("/new/reservationGoods")
    public BaseResponse<?> reservationNewServiceGoods(@RequestBody ReservationGoodsCreateRequest request) {
        ReservationGoodsResponse reservationGoods = reservationGoodsService.createReservationGoods(request);

        return new BaseResponse<>(reservationGoods);
    }
}
