package com.wooyano.wooyanomonolithic.reservation.domain.enumPackage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wooyano.wooyanomonolithic.global.common.CodeValue;
import lombok.Getter;

@Getter
public enum ReservationState implements CodeValue {

    PAYMENT_WAITING("0","결제대기"),
    WAIT("1","예약대기"),
    CONFIRMED("2","예약확정"),
    CANCEL("3","고객예약취소"),
    REFUSE("4","업체예약거절"),
    COMPLETE("5","서비스완료"),
    PAYMENT_CANCEL("6","결제실패");

    private final String code;
    private final String value;

    ReservationState(String code, String value) {
        this.code = code;
        this.value = value;
    }
    @JsonCreator
    public static ReservationState fromCode(String code) {
        for (ReservationState type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for code: " + code);
    }

    @JsonCreator
    public static ReservationState fromValue(String value) {
        for (ReservationState type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
