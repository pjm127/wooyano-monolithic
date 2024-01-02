package com.wooyano.wooyanomonolithic.reservation.presentation;

import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.reservation.application.reseravation.ReservationService;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "예약상품 예약",
            description = "유저의 예약상품 예약")
    @PostMapping("/create")
    public BaseResponse<?> reservationNewService(@RequestBody ReservationCreateRequest request) {
       reservationService.createReservation(request);

        return new BaseResponse<>();
    }

    @Operation(summary = "결제 후 예약 상태 변경",
            description = "결제 후 예약 상태 변경")
    @GetMapping("/success")
    public BaseResponse<?> reservationChangeService(@RequestParam(value = "orderId") String orderId,
                                                    @RequestParam(value = "amount") Integer amount,
                                                    @RequestParam(value = "paymentKey") String paymentKey) {

        reservationService.approveReservation(orderId, amount, paymentKey);

        return new BaseResponse<>();
    }


    @DeleteMapping("/cancel/{orderId}")
    public BaseResponse<?> reservationCancelService(@PathVariable String orderId) {

        reservationService.cancelReservation(orderId);

        return new BaseResponse<>();
    }



    //clientEmail로 예약 대기인거 조회 리스트
    @GetMapping("/wait-list")
    public List<ReservationListResponse>  getReservationList(@RequestParam Long serviceId) {
        return reservationService.findWaitReservationsList(serviceId);

    }




}
