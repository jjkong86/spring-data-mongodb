package com.me.springdata.mongodb.repository.userdetail;

import com.me.springdata.mongodb.document.UserDetail;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long> {

    UserDetail findByUserId(Long userId);
}
