package com.wooyano.wooyanomonolithic.reservation.application.reseravation;



import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.NOT_FOUND_WORKER;
import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.PAYMENT_AMOUNT_MISMATCH;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.config.toss.TossPaymentConfig;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
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


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {


    /*private static final int RANDOM_STRING_LENGTH = 10;
    private static final int ALPHABET_COUNT = 26;
    private static final int ASCII_LOWER_A = 97;*/

    private final RedisService redisService;

    private final ReservationRepository reservationRepository;
    private final ReservationGoodsRepository reservationGoodsRepository;
    private final PaymentRepository paymentRepository;
    private final WorkerTimeRepository workerTimeRepository;
    private final TossPaymentConfig tossPaymentConfig;


    public void checkWorkerAvailability(Worker worker, LocalDate reservationDate, LocalTime serviceStart) {
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerAndServiceTime(worker, serviceStart,reservationDate);
        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }
    }

    public void verifyPayment(String orderId, int amount) {
        String values = redisService.getValues(orderId);
        int saveAmount= Integer.parseInt(values);
        if (!Objects.equals(saveAmount, amount)) {
            throw new CustomException(PAYMENT_AMOUNT_MISMATCH);
        }
    }


    // 작업자+예약 저장 및 결제 승인 및 결제,주문저장
    @Transactional
    @Override
    public ReservationResponse saveWorkTimeAndReservationAndPayment(String paymentKey, String orderId, int amount,
                                                                Long serviceId, Long workerId, String userEmail,
                                                                LocalDate reservationDate, String request, String address,
                                                                String clientEmail, LocalTime serviceStart,
                                                                List<Long> reservationGoodsId,int suppliedAmount, int vat
                                                                ,String status, String method, Worker worker){

        saveReservedTime(reservationDate, serviceStart, worker);
        Reservation reservation = saveReservation(orderId, amount, serviceId, userEmail, reservationDate, request,
                address, serviceStart, reservationGoodsId, worker);
        savePayment(amount,clientEmail,orderId,paymentKey, suppliedAmount, vat,status,method);
        //throw new RuntimeException();
        return ReservationResponse.of(reservation);
    }

    private void saveReservedTime(LocalDate reservationDate, LocalTime serviceStart, Worker worker) {
        WorkerTime saveWorkerTime = WorkerTime.createWorkerTime(serviceStart, worker, reservationDate);
        workerTimeRepository.save(saveWorkerTime);
    }

    private void savePayment(int amount, String clientEmail, String orderId,String paymentKey,
                             int suppliedAmount, int vat,String status, String method) {
        PaymentMethod paymentMethod = PaymentMethod.fromCode(method);
        PaymentStatus paymentStatus = PaymentStatus.fromCode(status);
        Payment payment = Payment.builder()
                .totalAmount(amount)
                .paymentStatus(paymentStatus)
                .paymentType(paymentMethod)
                .clientEmail(clientEmail)
                .paymentKey(paymentKey)
                .orderId(orderId)
                .suppliedAmount(suppliedAmount)
                .vat(vat)
                .build();
        paymentRepository.save(payment);
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



}