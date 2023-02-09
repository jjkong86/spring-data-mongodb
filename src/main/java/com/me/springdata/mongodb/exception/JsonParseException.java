package com.me.springdata.mongodb.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class JsonParseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public JsonParseException(String message, Exception exception) {
        super(message, exception);
    }
}
