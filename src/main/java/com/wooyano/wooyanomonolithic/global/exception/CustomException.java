package com.wooyano.wooyanomonolithic.global.exception;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ResponseCode responseCode;

}