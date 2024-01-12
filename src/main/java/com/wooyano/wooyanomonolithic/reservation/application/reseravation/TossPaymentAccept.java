package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TossPaymentAccept {

    private final TossPaymentConfig tossPaymentConfig;


    //토스페이먼츠 외부 api 결제 승인 요청
    public PaymentResponse requestPaymentAccept(String paymentKey, String orderId, Integer amount) {
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
