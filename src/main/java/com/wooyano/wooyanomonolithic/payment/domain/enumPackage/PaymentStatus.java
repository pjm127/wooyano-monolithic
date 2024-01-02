package com.wooyano.wooyanomonolithic.payment.domain.enumPackage;


import com.wooyano.wooyanomonolithic.global.common.CodeValue;

public enum PaymentStatus implements CodeValue {
    WAIT("0","결제대기"),
    DONE("1","DONE"),
    CANCEL("2","결제취소");



    private final String code;
    private final String value;

    PaymentStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
    public static PaymentStatus fromCode(String value) {
        for (PaymentStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + value);
    }
}
