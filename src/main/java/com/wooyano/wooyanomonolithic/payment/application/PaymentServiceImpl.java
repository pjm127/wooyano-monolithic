package com.wooyano.wooyanomonolithic.payment.application;


import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.PAYMENT_AMOUNT_MISMATCH;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentCreateRequest;
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
import java.time.Duration;
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


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService  {

    private final PaymentRepository paymentRepository;

    private final WorkerRepository workerRepository;
    private final WorkerTimeRepository workerTimeRepository;
    private final RedisService redisService;


    @Transactional
    @Override
    public void savePaymentTemporarily(PaymentCreateRequest request) {
        Long workerId = request.getWorkerId();
        LocalTime serviceStartTime = request.getServiceStartTime();
        LocalDate reservationDate = request.getReservationDate();
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작업자입니다."));
        LocalDate today = LocalDate.now();
        //해당 작업자 시간 테이블 조회해서 없으면 저장 있으면 예외
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerAndServiceTime(worker,serviceStartTime,reservationDate);

        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }


        //결제 정보 저장
      /*  Payment payment = Payment.builder()
                .totalAmount(request.getPaymentAmount())
                .paymentStatus(PaymentStatus.WAIT)
                .paymentType(PaymentMethod.WAIT)
                .approvedAt(LocalDateTime.now())
                .clientEmail(request.getClientEmail()) //원래는 serviceId로 clientId찾아서 해야함
                .orderId(request.getOrderId()).build();
        paymentRepository.save(payment);*/
        String paymentKey = request.getOrderId();
        String paymentValue = String.valueOf(request.getPaymentAmount());
        Duration expirationDuration = Duration.ofMinutes(30);
        redisService.setValues(paymentKey, paymentValue, expirationDuration);
    }


    @Override
    public List<PaymentResultResponse> getPaymentsList() {
        return null;
    }
//    private final CommandGateway commandGateway;
//    private final BatchScheduler batchScheduler;




}
