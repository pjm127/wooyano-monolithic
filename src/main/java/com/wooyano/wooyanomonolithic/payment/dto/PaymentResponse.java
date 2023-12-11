package com.wooyano.wooyanomonolithic.payment.dto;


import lombok.Data;

@Data
public class PaymentResponse {
    private String mid; //있
    private String version;
    private String paymentKey; //있
    private String orderId; //있
    private String orderName; //있
    private String currency;
    private String method; //있
    private String totalAmount; //있
    private String balanceAmount;
    private String suppliedAmount;  //있
    private String vat; //있
    private String status; //있
    private String requestedAt; //있
    private String approvedAt; //있
    private String useEscrow;
    private String cultureExpense;
    private PaymentCardDto card;
    private String type; //있
}