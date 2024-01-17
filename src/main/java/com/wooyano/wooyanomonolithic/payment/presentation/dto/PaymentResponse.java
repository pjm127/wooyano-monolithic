package com.wooyano.wooyanomonolithic.payment.presentation.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String mid;
    private String version;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String currency;
    private String method;
    private int totalAmount;
    private int balanceAmount;
    private int suppliedAmount;
    private int vat;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String useEscrow;
    private String cultureExpense;
    private PaymentCardResponse card;
    private String type;
}