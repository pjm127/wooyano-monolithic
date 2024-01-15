package com.wooyano.wooyanomonolithic.reservation.presentation;

import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.reservation.application.reseravation.ReservationService;

import com.wooyano.wooyanomonolithic.reservation.application.reseravation.ReservationAccept;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationAccept tossRequestPaymentAccept;
    private final ReservationService reservationService;

    @Operation(summary = "결제 승인",
            description = "토스 api로 통신")
    @GetMapping("/success")
    public BaseResponse<?> reservationApproveService(  @RequestParam(name = "serviceId") Long serviceId,
                                                       @RequestParam(name = "workerId") Long workerId,
                                                       @RequestParam(name = "userEmail") String userEmail,
                                                       @RequestParam(name = "reservationDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reservationDate,
                                                       @RequestParam(name = "request") String request,
                                                       @RequestParam(name = "address") String address,
                                                       @RequestParam(name = "clientEmail") String clientEmail,
                                                       @RequestParam(name = "orderId") String orderId,
                                                       @RequestParam(name = "paymentKey") String paymentKey,
                                                       @RequestParam(name = "amount") int amount,
                                                       @RequestParam(name = "serviceStart") @DateTimeFormat(pattern = "HH:mm") LocalTime serviceStart,
                                                       @RequestParam(name = "reservationGoodsId") List<Long> reservationGoodsId) {

        ReservationResponse reservationResponse = tossRequestPaymentAccept.createReservation(paymentKey, orderId, amount,
                serviceId, workerId, userEmail, reservationDate, request, address, clientEmail, serviceStart,reservationGoodsId);

        return new BaseResponse<>(reservationResponse);
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
