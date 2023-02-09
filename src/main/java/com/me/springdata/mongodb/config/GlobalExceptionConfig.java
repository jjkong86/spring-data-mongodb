package com.me.springdata.mongodb.config;

import com.me.springdata.mongodb.exception.ValidCustomException;
import com.me.springdata.mongodb.response.ExceptionResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionConfig {
    @ExceptionHandler(value = ValidCustomException.class)
    public ExceptionResponse mismatchException(ValidCustomException exception) {
        return ExceptionResponse.builder().code(exception.getCode()).error(exception.getMessage()).build();
    }
}
