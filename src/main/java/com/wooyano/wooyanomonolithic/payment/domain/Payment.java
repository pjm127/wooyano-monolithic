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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_Email")
    private String clientEmail; //사업자 이메일
    @Column(name = "payment_Method")
    @Convert(converter = PaymentMethodConverter.class)
    private PaymentMethod paymentType; //결제수단  카드, 간편결제

    @Column(name = "total_Amount")
    private int totalAmount; //결제 금액


    @Column(name = "payment_Status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus; //결제 승인대기, 결제 완료, 취소

    @Column(name = "order_Id")
    private String orderId;

    @Column(name = "payment_Key")
    private String paymentKey;

    @Column(name = "supplied_Amount")
    private int suppliedAmount; //공급가액

    @Column(name = "vat")
    private int vat; //부가세 10퍼



    @Builder
    private Payment(String clientEmail, PaymentMethod payType, int totalAmount,
                    PaymentStatus payStatus, String orderId, String paymentKey,
                    int suppliedAmount, int vat) {
        this.clientEmail = clientEmail;
        this.paymentType = payType;
        this.totalAmount = totalAmount;
        this.paymentStatus = payStatus;
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.suppliedAmount = suppliedAmount;
        this.vat = vat;
    }

    public static Payment createPayment(String clientEmail, PaymentMethod payType, int totalAmount,
                                        PaymentStatus paymentStatus, String orderId,
                                        String paymentKey, int suppliedAmount, int vat) {
        return Payment.builder()
                .clientEmail(clientEmail)
                .paymentType(payType)
                .totalAmount(totalAmount)
                .paymentStatus(paymentStatus)
                .orderId(orderId)
                .paymentKey(paymentKey)
                .suppliedAmount(suppliedAmount)
                .vat(vat)
                .build();
    }


}
