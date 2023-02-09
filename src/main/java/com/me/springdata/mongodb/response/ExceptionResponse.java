package com.me.springdata.mongodb.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ExceptionResponse {
    int code;
    String error;

    @Builder
    public ExceptionResponse(int code, String error) {
        this.code = code;
        this.error = error;
    }
}
