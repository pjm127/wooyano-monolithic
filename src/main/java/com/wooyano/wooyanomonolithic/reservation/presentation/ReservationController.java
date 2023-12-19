package com.wooyano.wooyanomonolithic.reservation.presentation;

import com.wooyano.wooyanomonolithic.global.common.response.BaseResponse;
import com.wooyano.wooyanomonolithic.reservation.application.ReservationService;
import com.wooyano.wooyanomonolithic.reservation.dto.ChangeReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationDto;
import com.wooyano.wooyanomonolithic.reservation.dto.ReservationListResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "서비스 신청",
            description = "유저의 서비스 신청")
    @PostMapping("/create")
    public BaseResponse<?> reservationNewService(@RequestBody CreateReservationDto request) {
        String reservation = reservationService.createReservation(request);

        return new BaseResponse<>(reservation);
    }



/*    @Operation(summary = "결제 후 예약 상태 변경",
            description = "결제 후 예약 상태 변경")
    @PostMapping("/change")
    public BaseResponse<?> reservationChangeService(@RequestBody ChangeReservationRequest request) {

        reservationService.changeReservationStatus(request);

        return new BaseResponse<>();
    }*/


    //clientEmail로 예약 대기인거 조회 리스트
    @GetMapping("/wait-list")
    public   List<ReservationListResponse>  getReservationList(@RequestParam Long serviceId) {
        return reservationService.findWaitReservationsList(serviceId);

    }

/*
    @GetMapping("/sse")
    public SseEmitter handleSSE() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));

        return emitter;
    }

    public void sendPaymentKey(String paymentKey) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("cancel")
                        .data("{\"paymentKey\": \"" + paymentKey + "\"}"));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }*/
}
