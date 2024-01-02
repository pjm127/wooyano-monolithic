package com.wooyano.wooyanomonolithic.payment.application;


import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.PAYMENT_AMOUNT_MISMATCH;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final ReservationGoodsRepository reservationGoodsRepository;
    private final TossPaymentConfig tossPaymentConfig;

    private final WorkerRepository workerRepository;
    private final WorkerTimeRepository workerTimeRepository;

    //결제성공 후 결제 상태변경
    @Transactional
    @Override
    public PaymentResponse approvePayment(String paymentKey, String orderId, int amount,
                                          Long serviceId, Long workerId, String userEmail,
                                          LocalDate reservationDate, String request, String address,
                                          String clientEmail, LocalTime serviceStart,List<Long> reservationGoodsId){

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작업자입니다."));
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerAndServiceTime(worker,serviceStart);
        log.info("workerTime: {}",workerTime);
        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }
        else{
            WorkerTime saveWorkerTime = WorkerTime.createWorkerTime(serviceStart,worker);
            workerTimeRepository.save(saveWorkerTime);
        }



        Payment payment = verifyPayment(orderId, amount);
        PaymentResponse paymentResponse = requestPaymentAccept(paymentKey, orderId, amount);


        List<ReservationGoods> reservationGoods = reservationGoodsRepository.findByIdIn(reservationGoodsId);

        //예약정보 저장
        Reservation reservations = Reservation.createReservation(reservationGoods, userEmail,
                serviceId, worker,reservationDate, serviceStart,
                 amount,null,request, address,orderId);
        Reservation saveReservation = reservationRepository.save(reservations);
        log.info("save: {}",saveReservation);

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
                .approvedAt(LocalDateTime.now())
                .clientEmail(paymentRequest.getClientEmail()) //원래는 serviceId로 clientId찾아서 해야함
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
