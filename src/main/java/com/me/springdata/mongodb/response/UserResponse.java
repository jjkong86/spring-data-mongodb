package com.me.springdata.mongodb.response;

import com.me.springdata.mongodb.document.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse extends ApiCommonResponse {
    User user;

    @Builder
    public UserResponse(int code, String error, User user) {
        super(code, error);
        this.user = user;
    }
}
