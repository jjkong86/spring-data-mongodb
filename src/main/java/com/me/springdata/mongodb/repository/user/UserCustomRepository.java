package com.me.springdata.mongodb.repository.user;


import com.me.springdata.mongodb.document.User;

public interface UserCustomRepository {
    User findTemplateByUserId(Long userId);

    long updateLocByUserId(Long userId, String loc);
}
