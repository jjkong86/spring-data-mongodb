package com.me.springdata.mongodb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidCustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private int code;

    public ValidCustomException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }
}
