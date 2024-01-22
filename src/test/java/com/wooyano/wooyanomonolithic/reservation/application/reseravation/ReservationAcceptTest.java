package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import static com.wooyano.wooyanomonolithic.global.common.response.ResponseCode.DUPLICATED_RESERVATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import com.wooyano.wooyanomonolithic.global.config.redis.RedisService;
import com.wooyano.wooyanomonolithic.global.exception.CustomException;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.reservation.domain.Reservation;
import com.wooyano.wooyanomonolithic.reservation.dto.reservation.ReservationResponse;
import com.wooyano.wooyanomonolithic.reservation.infrastructure.ReservationRepository;
import com.wooyano.wooyanomonolithic.worker.domain.Worker;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerTimeRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationAcceptTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private WorkerRepository workerRepository;



    @Mock
    private TossPaymentAccept tossPaymentAccept;

    @InjectMocks
    private ReservationAccept reservationAccept;

    @DisplayName("예약 생성 테스트")
    @Test
    void createReservation() {
       Worker mockWorker = mock(Worker.class);
       when(workerRepository.findById(anyLong())).thenReturn(java.util.Optional.of(mockWorker));

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .payOutAmount(10000)
                .vat(1000)
                .status("DONE")
                .method("CARD")
                .approvedAt("2021-09-01T00:00:00.000000")
                .build();
        BDDMockito.given(tossPaymentAccept.requestPaymentAccept(anyString(), anyString(), anyInt()))
                    .willReturn(paymentResponse);

        String paymentKey = "samplePaymentKey";
        String orderId = "sampleOrderId";
        int amount = 10000;
        Long serviceId = 1L;
        Long workerId = 1L;
        String userEmail = "user@example.com";
        LocalDate reservationDate = LocalDate.now();
        String request = "Sample request";
        String address = "Sample address";
        String clientEmail = "client@example.com";
        LocalTime serviceStart = LocalTime.of(10, 0);
        LocalTime serviceEnd = LocalTime.of(11, 0);
        List<Long> reservationGoodsId = List.of(1l,2l);
        ReservationResponse reservationResponse = ReservationResponse.builder()
                .reservationId(1L)
                .orderId(orderId)
                .amount(amount)
                .serviceId(serviceId)
                .userEmail(userEmail)
                .reservationDate(reservationDate)
                .request(request)
                .address(address)
                .serviceStart(serviceStart)
                .workerId(workerId)
                .build();

        BDDMockito.given(reservationService.saveWorkTimeAndReservationAndPayment(
                        anyString(), anyString(), anyInt(),
                        anyLong(), anyLong(), anyString(),
                        any(LocalDate.class), anyString(), anyString(),
                        anyString(), any(LocalTime.class), any(LocalTime.class),any(List.class),
                        anyInt(), anyInt(), anyString(), anyString(),
                        any(Worker.class), anyString()))
                .willReturn(reservationResponse);

        // then
        ReservationResponse reservation = reservationAccept.createReservation(paymentKey, orderId, amount, serviceId,
                workerId, userEmail,
                reservationDate, request, address, clientEmail, serviceStart,serviceEnd, reservationGoodsId);
        //then
        assertThat(reservation.getAmount()).isEqualTo(10000);
        verify(workerRepository,times(1)).findById(anyLong());
    }
}