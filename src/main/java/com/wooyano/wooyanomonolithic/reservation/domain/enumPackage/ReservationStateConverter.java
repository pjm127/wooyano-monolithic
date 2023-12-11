package com.wooyano.wooyanomonolithic.reservation.domain.enumPackage;

import jakarta.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class ReservationStateConverter implements AttributeConverter<ReservationState, String> {
    @Override
    public String convertToDatabaseColumn(ReservationState attribute) {
        return attribute.getCode();
    }

    @Override
    public ReservationState convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(ReservationState.class).stream()
                .filter(c -> c.getCode().equals(dbData))
                .findFirst()
                .orElseThrow(()-> new NoSuchElementException("존재하지 않는 코드입니다."));
    }
}
