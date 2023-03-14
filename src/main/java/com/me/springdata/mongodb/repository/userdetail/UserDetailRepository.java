package com.me.springdata.mongodb.repository.userdetail;

import com.me.springdata.mongodb.document.UserDetail;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailRepository extends CrudRepository<UserDetail, Long> {

    UserDetail findByDetailId(Long detailId);
    List<UserDetail> findByDetailIdIn(List<Long> userIdList);
}
