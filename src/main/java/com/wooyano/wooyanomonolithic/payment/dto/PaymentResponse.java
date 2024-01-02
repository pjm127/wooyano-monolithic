package com.wooyano.wooyanomonolithic.payment.dto;


import lombok.Data;

@Data
public class PaymentResponse {
    private String mid;
    private String version;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String currency;
    private String method;
    private String totalAmount;
    private String balanceAmount;
    private String suppliedAmount;
    private String vat;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private String useEscrow;
    private String cultureExpense;
    private PaymentCardResponse card;
    private String type;
}