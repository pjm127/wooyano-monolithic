package com.wooyano.wooyanomonolithic.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * 200: 요청 성공
     **/
    SUCCESS(HttpStatus.OK,true, 200, "요청에 성공하였습니다."),


    /**
     * 에러 코드
     **/

    CANNOT_FIND_RESERVATION(HttpStatus.BAD_REQUEST,true, 3010, "예약번호로 조회되는 예약이 존재하지 않습니다."),
    CANNOT_FIND_RESERVATION_GOODS(HttpStatus.BAD_REQUEST, true, 3011, "예약 상품 번호로 조회되는 예약상품이 존재하지 않습니다."),
    DUPLICATED_RESERVATION(HttpStatus.BAD_REQUEST, false, 3012, "이미 예약된 서비스입니다."),
    CANNOT_FIND_RESERVATION_WORKER(HttpStatus.BAD_REQUEST, false, 3013, "예약번호로 조회되는 예약이 존재하지 않습니다."),

   
    INCORRECT_RESERVATION_STATE(HttpStatus.BAD_REQUEST, false, 3030, "잘못된 예약상태입니다."),
    WRONG_APPROACH(HttpStatus.BAD_REQUEST, false, 3040, "잘못된 접근입니다."),
    PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, false, 3050, "결제 금액이 일치하지 않습니다."),
    NOT_FOUND_SERVICE(HttpStatus.BAD_REQUEST, false, 3060, "서비스가 존재하지 않습니다."),
    NOT_FOUND_WORKER(HttpStatus.BAD_REQUEST, false, 3070, "작업자가 존재하지 않습니다."),;





    private final HttpStatus httpStatus;
    private final boolean success;
    private final int code;
    private final String message;

}
