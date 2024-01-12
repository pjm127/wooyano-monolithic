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
    private final TossPaymentConfig tossPaymentConfig;
    private final WorkerRepository workerRepository;
    private final WorkerTimeRepository workerTimeRepository;
    private final RedisService redisService;

    public ReservationResponse createReservation(String paymentKey, String orderId, int amount,
                     Long serviceId, Long workerId, String userEmail,
                     LocalDate reservationDate, String request, String address,
                     String clientEmail, LocalTime serviceStart,
                     List<Long> reservationGoodsId){
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_WORKER));

        checkWorkerAvailability(worker, reservationDate, serviceStart);
        verifyPayment(orderId, amount);

        PaymentResponse paymentResponse = requestPaymentAccept(paymentKey, orderId, amount); //토스 외부 api

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



    //토스페이먼츠 외부 api 결제 승인 요청
    private PaymentResponse requestPaymentAccept(String paymentKey, String orderId, Integer amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);


        String url = TossPaymentConfig.URL + "confirm" ; //"https://api.tosspayments.com/v1/payments/confirm"
        HttpEntity<String> jsonObjectHttpEntity = new HttpEntity<>(params.toString(), headers);

        PaymentResponse paymentResponse = restTemplate.postForObject(url,
                jsonObjectHttpEntity,
                PaymentResponse.class);
        return paymentResponse;

    }
    //헤더 필수값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes(StandardCharsets.UTF_8)));
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UUID randomUUID = UUID.randomUUID();
        headers.set("Idempotency-Key", randomUUID.toString());
        return headers;
    }

}


