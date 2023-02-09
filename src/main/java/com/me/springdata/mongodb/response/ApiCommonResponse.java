package com.me.springdata.mongodb.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ApiCommonResponse {
    int code = 200;
    String error;

    public ApiCommonResponse(int code, String error) {
        if (code == 0) code = 200;

        this.code = code;
        this.error = error;
    }
}
