package com.wooyano.wooyanomonolithic.payment.domain.enumPackage;


import com.wooyano.wooyanomonolithic.global.common.CodeValue;

public enum PaymentMethod implements CodeValue {

    WAIT("0","대기"),
    CARD("1","카드"),
    EASY_PAYMENT("2","간편결제");


    private final String code;
    private final String value;

    PaymentMethod(String code, String value) {
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

    public static PaymentMethod fromCode(String value) {
        for (PaymentMethod type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + value);
    }
}
