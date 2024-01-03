package com.wooyano.wooyanomonolithic.global.exception;

import com.wooyano.wooyanomonolithic.global.common.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class CustomException extends RuntimeException {

    private ResponseCode responseCode;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}