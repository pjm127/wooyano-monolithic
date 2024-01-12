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
    private final WorkerTimeRepository workerTimeRepository;
    private final RedisService redisService;
    private final TossPaymentAccept tossPaymentAccept;

    public ReservationResponse createReservation(String paymentKey, String orderId, int amount,
                     Long serviceId, Long workerId, String userEmail,
                     LocalDate reservationDate, String request, String address,
                     String clientEmail, LocalTime serviceStart,
                     List<Long> reservationGoodsId){
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_WORKER));

        checkWorkerAvailability(worker, reservationDate, serviceStart);
        verifyPayment(orderId, amount);

        PaymentResponse paymentResponse = tossPaymentAccept.requestPaymentAccept(paymentKey, orderId, amount);

        return reservationService.saveWorkTimeAndReservationAndPayment(paymentKey,
                orderId, amount, serviceId, workerId,
                userEmail, reservationDate, request, address, clientEmail, serviceStart, reservationGoodsId,paymentResponse.getSuppliedAmount(),
                paymentResponse.getVat(),paymentResponse.getStatus(),paymentResponse.getMethod(),worker,paymentResponse.getApprovedAt());

    }
    private void checkWorkerAvailability(Worker worker, LocalDate reservationDate, LocalTime serviceStart) {
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerAndServiceTime(worker, serviceStart,reservationDate);
        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }
    }

    private void verifyPayment(String orderId, int amount) {
        String values = redisService.getValues(orderId);
        int saveAmount= Integer.parseInt(values);
        if (!Objects.equals(saveAmount, amount)) {
            throw new CustomException(PAYMENT_AMOUNT_MISMATCH);
        }
    }





}


