package com.wooyano.wooyanomonolithic.payment.domain.enumPackage;

import jakarta.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class PaymentTypeConverter implements AttributeConverter<PaymentType, String> {

    @Override
    public String convertToDatabaseColumn(PaymentType attribute) {
        return attribute.getCode();
    }


    @Override
    public PaymentType convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(PaymentType.class).stream()
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 결제 타입입니다."));
    }
}
