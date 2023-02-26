package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Log4j2
public class UserDetailTransactionService {
    private final UserDetailRepository userDetailRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void newTransactionTest(Long userId, boolean rollback) {
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        userDetail.setLoc(null);
        userDetailRepository.save(userDetail);
        if (rollback) {
            throw new RuntimeException("UserDetail loc update rollback. userId : " + userId);
        }
    }
}
