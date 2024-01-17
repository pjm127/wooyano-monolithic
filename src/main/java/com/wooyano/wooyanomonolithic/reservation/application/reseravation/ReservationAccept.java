package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.NOT_FOUND_WORKER;
import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.PAYMENT_AMOUNT_MISMATCH;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class ReservationAccept {
    private final ReservationService reservationService;
    private final WorkerRepository workerRepository;

    private final TossPaymentAccept tossPaymentAccept;

    public ReservationResponse createReservation(String paymentKey, String orderId, int amount,
                     Long serviceId, Long workerId, String userEmail,
                     LocalDate reservationDate, String request, String address,
                     String clientEmail, LocalTime serviceStart,LocalTime serviceEnd,
                     List<Long> reservationGoodsId){
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_WORKER));

        reservationService.checkWorkerAvailability(worker, reservationDate, serviceStart);
        reservationService.verifyPayment(orderId, amount);

        PaymentResponse paymentResponse = tossPaymentAccept.requestPaymentAccept(paymentKey, orderId, amount);

        return reservationService.saveWorkTimeAndReservationAndPayment(paymentKey,
                orderId, amount, serviceId, workerId,
                userEmail, reservationDate, request, address, clientEmail, serviceStart,serviceEnd, reservationGoodsId,paymentResponse.getSuppliedAmount(),
                paymentResponse.getVat(),paymentResponse.getStatus(),paymentResponse.getMethod(),worker,paymentResponse.getApprovedAt());

    }





}


