package com.wooyano.wooyanomonolithic.payment.application;


import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.PAYMENT_AMOUNT_MISMATCH;

import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService  {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final TossPaymentConfig tossPaymentConfig;

    @Transactional
    @Override
    public PaymentResponse approvePayment(String paymentKey, String orderId, Integer amount) {
        Payment payment = verifyPayment(orderId, amount);
        PaymentResponse paymentResponse = requestPaymentAccept(paymentKey, orderId, amount);
        String method = paymentResponse.getMethod(); //간단결제
        String status = paymentResponse.getStatus(); //DONE
        PaymentMethod paymentMethod = PaymentMethod.fromCode(method);
        PaymentStatus paymentStatus = PaymentStatus.fromCode(status);
        payment.approvePayment(paymentKey,paymentStatus, paymentMethod);
        return paymentResponse;
    }

    //토스페이먼츠 외부 api 결제 승인 요청
    private PaymentResponse requestPaymentAccept(String paymentKey, String orderId, Integer amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);

        log.info("params : {}", params);
        String u = TossPaymentConfig.URL + "confirm" ; //"https://api.tosspayments.com/v1/payments/confirm"
        log.info("url : {}", u);
        HttpEntity<String> jsonObjectHttpEntity = new HttpEntity<>(params.toString(), headers);
        log.info("jsonObjectHttpEntity : {}", jsonObjectHttpEntity);

        PaymentResponse paymentSuccessDto = restTemplate.postForObject(u,
                jsonObjectHttpEntity,
                PaymentResponse.class);
        log.info("paymentSuccessDto : {}", paymentSuccessDto);
        return paymentSuccessDto;

    }
    //헤더 필수값
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        log.info("header secret : {}", tossPaymentConfig.getTestSecretApiKey());
        String encodedAuthKey = new String(
                Base64.getEncoder().encode((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes(StandardCharsets.UTF_8)));
        log.info("encodedAuthKey : {}", encodedAuthKey);
        headers.setBasicAuth(encodedAuthKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        UUID randomUUID = UUID.randomUUID();
        headers.set("Idempotency-Key", randomUUID.toString());

        return headers;
    }

    private Payment verifyPayment(String orderId, Integer amount) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        log.info("payment : {}", payment.getOrderId());
        if (!Objects.equals(payment.getTotalAmount(), amount)) {
            throw new CustomException(PAYMENT_AMOUNT_MISMATCH);
        }
        return payment;
    }

    @Override
    public void savePayment(PaymentRequest paymentRequest) {
        log.info("paymentRequest : {}", paymentRequest.getTotalAmount());
        log.info("paymentRequest : {}", paymentRequest.getOrderId());

        Payment payment = Payment.builder()
                .totalAmount(paymentRequest.getTotalAmount())
                .paymentStatus(PaymentStatus.WAIT)
                .paymentType(PaymentMethod.WAIT)
                .orderId(paymentRequest.getOrderId()).build();
        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentResultResponse> getPaymentsList() {
        return null;
    }
//    private final CommandGateway commandGateway;
//    private final BatchScheduler batchScheduler;




}
