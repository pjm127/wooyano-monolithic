package com.wooyano.wooyanomonolithic.reservation.application;



import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.CANNOT_FIND_RESERVATION_GOODS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.domain.Payment;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentType;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.domain.ReservationGoods;
import com.wooyano.wooyanomonolithic.reservation.domain.enumPackage.ReservationState;
import com.wooyano.wooyanomonolithic.reservation.dto.ChangeReservationRequest;
import com.wooyano.wooyanomonolithic.reservation.dto.CreateReservationDto;
import com.wooyano.wooyanomonolithic.reservation.dto.ReservationListResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationGoodsRepository;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {


    private static final int RANDOM_STRING_LENGTH = 10;
    private static final int ALPHABET_COUNT = 26;
    private static final int ASCII_LOWER_A = 97;


    private final ReservationRepository reservationRepository;
    private final ReservationGoodsRepository reservationGoodsRepository;

    private final PaymentRepository paymentRepository;

    @Override
    public String createReservation(CreateReservationDto request) {
        log.info("createReservation");
        List<Long> reservationGoodsIdList = request.getReservationGoodsId();
        Long workerId = request.getWorkerId();

        validateDuplicateReservationGoodsWithWorker(reservationGoodsIdList, workerId);

        reservationGoodsIdList.stream().map(reservationGoodsId->{
            ReservationGoods reservationGoods = reservationGoodsRepository.findById(reservationGoodsId)
                    .orElseThrow(() -> new CustomException(CANNOT_FIND_RESERVATION_GOODS));
            return Reservation.builder()
                    .reservationGoods(reservationGoods)
                    .userEmail(request.getUserEmail())
                    .serviceId(request.getServiceId())
                    .workerId(request.getWorkerId())
                    .reservationDate(request.getReservationDate())
                    .serviceStart(request.getServiceStart())
                    .serviceEnd(request.getServiceEnd())
                    .reservationState(ReservationState.PAYMENT_WAITING)
                    .paymentAmount(request.getPaymentAmount())
                    .request(request.getRequest())
                    .orderId(request.getOrderId())
                    .address(request.getAddress())
                    .build();
        }).forEach(reservationRepository::save);

        Payment payment = Payment.builder()
                .totalAmount(request.getPaymentAmount())
                .paymentStatus(PaymentStatus.WAIT)
                .paymentType(PaymentType.WAIT)
                .orderId(request.getOrderId()).build();
        paymentRepository.save(payment);
        return request.getOrderId();
    }




    @Override
    public List<ReservationListResponse> findWaitReservationsList(Long serviceId) {
        return null;
    }

    @Override
    public void processReservationDecisionEvent(String consumerRecord) throws JsonProcessingException {

    }

    //작업자+예약 상품 중복 검사
    private void validateDuplicateReservationGoodsWithWorker(List<Long> reservationGoodsIdList, Long workerId) {
        boolean isDuplicateReservationGoods = reservationGoodsIdList.stream()
                .anyMatch(reservationGoodsId ->  reservationRepository.findByReservationGoodsId(reservationGoodsId, workerId).isPresent());

        if (isDuplicateReservationGoods) {
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION);
        }
    }
    // 랜덤 예약번호 생성
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
    }
}