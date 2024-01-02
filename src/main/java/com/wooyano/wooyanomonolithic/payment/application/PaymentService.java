package com.wooyano.wooyanomonolithic.payment.application;

import com.wooyano.wooyanomonolithic.payment.dto.PaymentRequest;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResponse;
import com.wooyano.wooyanomonolithic.payment.dto.PaymentResultResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface PaymentService {
    PaymentResponse approvePayment(String paymentKey, String orderId, int amount,
                                   Long serviceId, Long workerId, String userEmail,
                                   LocalDate reservationDate, String request, String address,
                                   String clientEmail, LocalTime serviceStart,List<Long> reservationGoodsId);
    void savePayment(PaymentRequest paymentRequest);

    List<PaymentResultResponse> getPaymentsList();

}
