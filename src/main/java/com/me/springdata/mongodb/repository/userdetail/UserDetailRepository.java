package com.me.springdata.mongodb.repository.userdetail;

import com.me.springdata.mongodb.document.UserDetail;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long> {

    UserDetail findByUserId(Long userId);
    List<UserDetail> findAllByUserIdIn(List<Long> userIdList);
}
