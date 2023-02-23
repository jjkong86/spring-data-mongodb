package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserTransactionService {
    private final UserTemplateRepository templateRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public long userUpdate(Long userId) {
        long l = templateRepository.updateLocByUserId(userId, null);
        User user = templateRepository.findByUserId(userId);
        if (user.getUserId() == 2L) {
            throw new RuntimeException("userId == " + userId + " is not update.");
        }

        return l;
    }

    @Transactional
    public void multiDocumentTransactionTest(Long userId) {
        User user = templateRepository.findByUserId(userId);
        user.setLocList(null);
        UserDetail userDetail = userDetailRepository.findByUserId(userId);
        userDetail.setLoc(null);
        userRepository.save(user);
        userDetailRepository.save(userDetail);
        if (user.getUserId() == 2L) {
            throw new RuntimeException("userId == " + userId + " is not update.");
        }
    }
}
