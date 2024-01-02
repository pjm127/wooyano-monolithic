package com.wooyano.wooyanomonolithic.reservation.application.reseravation;



import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.CANNOT_FIND_RESERVATION_GOODS;
import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.PAYMENT_AMOUNT_MISMATCH;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
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
import java.util.ArrayList;
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


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {


    /*private static final int RANDOM_STRING_LENGTH = 10;
    private static final int ALPHABET_COUNT = 26;
    private static final int ASCII_LOWER_A = 97;*/


    private final ReservationRepository reservationRepository;
    private final ReservationGoodsRepository reservationGoodsRepository;
    private final PaymentRepository paymentRepository;
    private final WorkerRepository workerRepository;
    private final WorkerTimeRepository workerTimeRepository;
    private final TossPaymentConfig tossPaymentConfig;


    // 작업자+예약 저장 및 결제 승인 및 결제,주문저장
    @Transactional
    @Override
    public ReservationResponse saveWorkTimeAndReservationAndPayment(String paymentKey, String orderId, int amount,
                                                                Long serviceId, Long workerId, String userEmail,
                                                                LocalDate reservationDate, String request, String address,
                                                                String clientEmail, LocalTime serviceStart, List<Long> reservationGoodsId){

        Worker worker = checkAndUpdateWorkerAvailability(workerId, reservationDate, serviceStart);
        Payment payment = verifyPayment(orderId, amount);
        PaymentResponse paymentResponse = requestPaymentAccept(paymentKey, orderId, amount);

        Reservation reservation = saveReservation(orderId, amount, serviceId, userEmail, reservationDate, request,
                address, serviceStart, reservationGoodsId, worker);

        approvePayment(paymentKey, payment, paymentResponse);
        return ReservationResponse.of(reservation);
    }

    private static void approvePayment(String paymentKey, Payment payment, PaymentResponse paymentResponse) {
        String method = paymentResponse.getMethod(); //간단결제
        String status = paymentResponse.getStatus(); //DONE
        PaymentMethod paymentMethod = PaymentMethod.fromCode(method);
        PaymentStatus paymentStatus = PaymentStatus.fromCode(status);
        payment.approvePayment(paymentKey,paymentStatus, paymentMethod);
    }

    private Reservation saveReservation(String orderId, int amount, Long serviceId, String userEmail,
                                        LocalDate reservationDate, String request, String address,
                                        LocalTime serviceStart, List<Long> reservationGoodsId, Worker worker) {
        List<ReservationGoods> reservationGoods = reservationGoodsRepository.findByIdIn(reservationGoodsId);

        //예약정보 저장
        Reservation reservations = Reservation.createReservation(reservationGoods, userEmail,
                serviceId, worker, reservationDate, serviceStart,
                amount,null, request, address, orderId);
        return reservationRepository.save(reservations);

    }

    private Worker checkAndUpdateWorkerAvailability(Long workerId, LocalDate reservationDate, LocalTime serviceStart) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작업자입니다."));
        LocalDate today = LocalDate.now();
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerAndServiceTime(worker, serviceStart,today);

        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }
        else{
            WorkerTime saveWorkerTime = WorkerTime.createWorkerTime(serviceStart,worker, reservationDate);
            workerTimeRepository.save(saveWorkerTime);
        }
        return worker;
    }


    //토스페이먼츠 외부 api 결제 승인 요청
    private PaymentResponse requestPaymentAccept(String paymentKey, String orderId, Integer amount) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        JSONObject params = new JSONObject();
        params.put("paymentKey", paymentKey);
        params.put("orderId", orderId);
        params.put("amount", amount);


        String u = TossPaymentConfig.URL + "confirm" ; //"https://api.tosspayments.com/v1/payments/confirm"
        HttpEntity<String> jsonObjectHttpEntity = new HttpEntity<>(params.toString(), headers);

        PaymentResponse paymentSuccessDto = restTemplate.postForObject(u,
                jsonObjectHttpEntity,
                PaymentResponse.class);
        return paymentSuccessDto;

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

    private Payment verifyPayment(String orderId, Integer amount) {
        Payment payment = paymentRepository.findByOrderId(orderId);
        log.info("payment : {}", payment.getOrderId());
        if (!Objects.equals(payment.getTotalAmount(), amount)) {
            throw new CustomException(PAYMENT_AMOUNT_MISMATCH);
        }
        return payment;
    }


    @Transactional
    @Override
    public void approveReservation(String orderId, Integer amount, String paymentKey) {
        Reservation reservation = reservationRepository.findByOrderIdList(orderId);
        Payment payment = paymentRepository.findByOrderId(orderId);
        reservation.approveStatus(ReservationState.WAIT);

    }

    @Override
    public List<ReservationListResponse> findWaitReservationsList(Long serviceId) {
        return null;
    }

    @Transactional
    @Override
    public void cancelReservation(String orderId) {
        Reservation reservation = reservationRepository.findByOrderIdList(orderId);
        reservation.approveStatus(ReservationState.PAYMENT_CANCEL);

    }


    //예약상품 리스트 안에 상품 있는지부터 확인
    private void validateReservationGoodsExistence(List<Long> reservationGoodsIdList) {
        boolean allReservationGoodsExist = reservationGoodsIdList.stream()
                .allMatch(reservationGoodsId -> reservationGoodsRepository.findById(reservationGoodsId).isPresent());

        if (!allReservationGoodsExist) {
            throw new CustomException(CANNOT_FIND_RESERVATION_GOODS);
        }
    }

    //작업자+예약 상품 중복 검사
  /*  private void validateDuplicateReservationGoodsWithWorker(List<Long> reservationGoodsIdList, Long workerId) {
        boolean isDuplicateReservationGoods = reservationGoodsIdList.stream()
                .anyMatch(reservationGoodsId ->  reservationRepository.findByReservationGoodsId(reservationGoodsId, workerId).isPresent());

        if (isDuplicateReservationGoods) {
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION);
        }
    }*/
  /*  // 랜덤 예약번호 생성
    private String generateRandomReservationNum() {
        Random random = new Random();
        StringBuilder randomBuf = new StringBuilder();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            if (random.nextBoolean()) {
                randomBuf.append((char) ((int) (random.nextInt(ALPHABET_COUNT)) + ASCII_LOWER_A));
            } else {
                randomBuf.append(random.nextInt(10));
            }
        }
        return randomBuf.toString();
    }*/
}