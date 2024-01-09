package com.wooyano.wooyanomonolithic.payment.application;

import com.wooyano.wooyanomonolithic.payment.application.dto.PaymentCreateServiceRequest;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentCreateRequest;
import com.wooyano.wooyanomonolithic.payment.presentation.dto.PaymentResultResponse;
import java.util.List;


public interface PaymentService {


    void savePaymentTemporarily(PaymentCreateServiceRequest request);

    List<PaymentResultResponse> getPaymentsList();

}
