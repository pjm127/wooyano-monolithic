package com.wooyano.wooyanomonolithic.payment.domain.enumPackage;

import jakarta.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, String> {

    @Override
    public String convertToDatabaseColumn(PaymentStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public PaymentStatus convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(PaymentStatus.class).stream()
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 결제 상태입니다."));
    }

}
