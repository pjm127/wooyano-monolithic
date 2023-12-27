package com.wooyano.wooyanomonolithic.reservation.application;



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
import com.wooyano.wooyanomonolithic.reservation.dto.PaymentCompletionRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationResponse;
import com.wooyano.wooyanomonolithic.reservation.dto.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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


    @Transactional
    @Override
    public CreateReservationResponse createReservation(CreateReservationRequest request) {
        log.info("createReservation");
        List<Long> reservationGoodsIdList = request.getReservationGoodsId();
        Long workerId = request.getWorkerId();

        validateReservationGoodsExistence(reservationGoodsIdList);
        validateDuplicateReservationGoodsWithWorker(reservationGoodsIdList, workerId);
        List<ReservationGoods> reservationGoods = reservationGoodsRepository.findByIdIn(reservationGoodsIdList);

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
        return CreateReservationResponse.of(save);
    }

    @Transactional
    @Override
    public void approveReservation(PaymentCompletionRequest request) {
        Reservation reservation = reservationRepository.findByOrderIdList(request.getOrderId());
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
    private void validateDuplicateReservationGoodsWithWorker(List<Long> reservationGoodsIdList, Long workerId) {
        boolean isDuplicateReservationGoods = reservationGoodsIdList.stream()
                .anyMatch(reservationGoodsId ->  reservationRepository.findByReservationGoodsId(reservationGoodsId, workerId).isPresent());

        if (isDuplicateReservationGoods) {
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION);
        }
    }
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