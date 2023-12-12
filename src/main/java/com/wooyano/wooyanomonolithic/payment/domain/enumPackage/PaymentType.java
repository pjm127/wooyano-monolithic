package com.wooyano.wooyanomonolithic.payment.domain.enumPackage;


import com.wooyano.wooyanomonolithic.global.common.CodeValue;

public enum PaymentType implements CodeValue {

    WAIT("0","대기"),
    CARD("1","카드"),
    EASY_PAYMENT("2","간편결제");


    private final String code;
    private final String value;

    PaymentType(String code, String value) {
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

    public static PaymentType fromCode(String code) {
        for (PaymentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + code);
    }
}
