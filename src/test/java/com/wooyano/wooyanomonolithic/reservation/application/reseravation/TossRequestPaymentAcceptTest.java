package com.wooyano.wooyanomonolithic.reservation.application.reseravation;

import com.wooyano.wooyanomonolithic.worker.infrastructure.WorkerRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class TossRequestPaymentAcceptTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private ReservationService internalReservationService;

    @Mock
    private ReservationAccept paymentService;


}