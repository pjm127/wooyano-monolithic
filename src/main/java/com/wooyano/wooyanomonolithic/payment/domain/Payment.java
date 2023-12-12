package com.wooyano.wooyanomonolithic.payment.domain;

import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatus;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentStatusConverter;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentType;
import com.wooyano.wooyanomonolithic.payment.domain.enumPackage.PaymentTypeConverter;
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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_Email")
    private String clientEmail; //사업자 이메일
    @Column(name = "payment_Type")
    @Convert(converter = PaymentTypeConverter.class)
    private PaymentType paymentType; //결제수단  카드, 간편결제

    @Column(name = "total_Amount")
    private int totalAmount; //결제 금액

    @Column(name = "approved_At")
    private LocalDateTime approvedAt; //결제 완료,취소가 일어난 날짜와 시간 정보

    @Column(name = "payment_Status")
    @Convert(converter = PaymentStatusConverter.class)
    private PaymentStatus paymentStatus; //결제 승인대기, 결제 완료, 취소

    @Column(name = "order_Id")
    private String orderId;

    private Payment(String clientEmail, PaymentType payType, int totalAmount,
                    LocalDateTime approvedAt, PaymentStatus payStatus,String orderId) {
        this.clientEmail = clientEmail;
        this.paymentType = payType;
        this.totalAmount = totalAmount;
        this.approvedAt = approvedAt;
        this.paymentStatus = payStatus;
        this.orderId = orderId;
    }

    public static Payment createPayment(String clientEmail, PaymentType payType, int totalAmount,
                                        LocalDateTime approvedAt, PaymentStatus paymentStatus,String orderId) {
        return new Payment(clientEmail, payType,
                totalAmount, approvedAt,paymentStatus,orderId);
    }

    public void finishSettlement(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
