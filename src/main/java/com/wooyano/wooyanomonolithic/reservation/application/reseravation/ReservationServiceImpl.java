package com.wooyano.wooyanomonolithic.reservation.application.reseravation;



import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.CANNOT_FIND_RESERVATION_GOODS;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationCreateResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional
    @Override
    public ReservationCreateResponse createReservation(ReservationCreateRequest request) {
        log.info("createReservation");
        List<Long> reservationGoodsIdList = request.getReservationGoodsId();
        Long workerId = request.getWorkerId();
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작업자입니다."));
       // validateReservationGoodsExistence(reservationGoodsIdList);
        //validateDuplicateReservationGoodsWithWorker(reservationGoodsIdList, workerId);

        //해당 작업자 시간 테이블 조회해서 없으면 저장 있으면 예외
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerId(workerId);
        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }
        else{
            WorkerTime saveWorkerTime = WorkerTime.createWorkerTime(request.getServiceStart(),worker);
            workerTimeRepository.save(saveWorkerTime);
        }


        //작업가능 시간 확인용 중간테이블 저장
        List<ReservationGoods> reservationGoods = reservationGoodsRepository.findByIdIn(reservationGoodsIdList);


        //예약정보 저장
        Reservation reservations = Reservation.createReservation(reservationGoods, request.getUserEmail(),
                request.getServiceId(), request.getWorkerId(), request.getReservationDate(), request.getServiceStart(),
                request.getServiceEnd(), request.getPaymentAmount(),null,request.getRequest(),
                request.getAddress(),request.getOrderId());
        Reservation save = reservationRepository.save(reservations);

        //결제 정보 저장
        Payment payment = Payment.builder()
                .totalAmount(request.getPaymentAmount())
                .paymentStatus(PaymentStatus.WAIT)
                .paymentType(PaymentMethod.WAIT)
                .approvedAt(LocalDateTime.now())
                .clientEmail(request.getClientEmail()) //원래는 serviceId로 clientId찾아서 해야함
                .orderId(request.getOrderId()).build();
        paymentRepository.save(payment);
        return ReservationCreateResponse.of(save);
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