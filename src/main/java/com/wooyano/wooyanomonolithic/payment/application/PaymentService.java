package com.wooyano.wooyanomonolithic.payment.application;

import com.wooyano.wooyanomonolithic.payment.dto.PaymentCreateRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface PaymentService {


    void savePaymentTemporarily(PaymentCreateRequest reservationNewServiceRequest);


    List<PaymentResultResponse> getPaymentsList();

}
