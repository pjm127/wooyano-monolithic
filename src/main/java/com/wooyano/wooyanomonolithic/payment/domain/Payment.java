package com.wooyano.wooyanomonolithic.payment.domain;

import com.wooyano.wooyanomonolithic.global.common.domain.BaseEntity;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatusConverter;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethod;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentMethodConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_Email")
    private String clientEmail; //사업자 이메일
    @Column(name = "payment_Method")
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentMethod; //결제수단  카드, 간편결제

    @Column(name = "total_Amount")
    private int totalAmount; //결제 금액


    @Column(name = "payment_Status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus; //결제 승인대기, 결제 완료, 취소

    @Column(name = "order_Id")
    private String orderId;

    @Column(name = "payment_Key")
    private String paymentKey;
    private int payOutAmount ; //지급 금액 결제 금액 amount에서 수수료인 fee를 제외한 금액

    private int fee; // 수수료


    private LocalDateTime approvedAt; //결제 승인 날짜 시간


    @Builder
    private Payment(String clientEmail, PaymentMethod paymentMethod, int totalAmount, PaymentStatus paymentStatus,
                   String orderId, String paymentKey, int payOutAmount, int fee, LocalDateTime approvedAt) {
        this.clientEmail = clientEmail;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.payOutAmount = payOutAmount;
        this.fee = fee;
        this.approvedAt = approvedAt;
    }


    public static Payment createPayment(String clientEmail, PaymentMethod paymentMethod, int totalAmount,
                                        PaymentStatus paymentStatus, String orderId,
                                        String paymentKey, int payOutAmount, int fee, LocalDateTime approvedAt ) {
        return Payment.builder()
                .clientEmail(clientEmail)
                .paymentMethod(paymentMethod)
                .totalAmount(totalAmount)
                .paymentStatus(paymentStatus)
                .orderId(orderId)
                .paymentKey(paymentKey)
                .payOutAmount(payOutAmount)
                .fee(fee)
                .approvedAt (approvedAt )
                .build();
    }


}
