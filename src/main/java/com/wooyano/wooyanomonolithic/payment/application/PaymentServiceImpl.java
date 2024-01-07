package com.wooyano.wooyanomonolithic.payment.application;


import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.application.dto.PaymentCreateServiceRequest;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentCreateRequest;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResultResponse;
import com.wooyano.wooyanomonolithic.payment.infrastructure.PaymentRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.domain.WorkerTime;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public void savePaymentTemporarily(PaymentCreateServiceRequest request) {
        Long workerId = request.getWorkerId();
        LocalTime serviceStartTime = request.getServiceStartTime();
        LocalDate reservationDate = request.getReservationDate();
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 작업자입니다."));

        //해당 작업자 시간 테이블 조회해서 없으면 저장 있으면 예외
        Optional<WorkerTime> workerTime = workerTimeRepository.findByWorkerAndServiceTime(worker,serviceStartTime,reservationDate);

        if(workerTime.isPresent()){
            throw new CustomException(ResponseCode.DUPLICATED_RESERVATION); //작업자는 해당시간에 작업 있음
        }


        //결제 정보 임시 저장
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
