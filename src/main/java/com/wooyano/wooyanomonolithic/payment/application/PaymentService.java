package com.wooyano.wooyanomonolithic.payment.application;

import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import java.util.List;


public interface PaymentService {
    PaymentResponse approvePayment(String paymentKey, String orderId, Long amount);
    void savePayment(PaymentRequest paymentRequest);

    List<PaymentResultResponse> getPaymentsList();

}
