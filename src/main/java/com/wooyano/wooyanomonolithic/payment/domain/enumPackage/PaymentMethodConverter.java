package com.wooyano.wooyanomonolithic.payment.domain.enumPackage;

import jakarta.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {

    @Override
    public String convertToDatabaseColumn(PaymentMethod attribute) {
        return attribute.getCode();
    }


    @Override
    public PaymentMethod convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(PaymentMethod.class).stream()
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 결제 타입입니다."));
    }
}
