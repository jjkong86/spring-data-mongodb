package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Log4j2
public class UserTransactionService {

    private final UserTemplateRepository templateRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserRepository userRepository;

    @Transactional
    public void userLocListUpdate(Long userId) {
        templateRepository.updateLocByUserId(userId, null);
    }

    @Transactional
    public void singleTransactionTest(Long userId, boolean rollback) {
        templateRepository.updateLocByUserId(userId, null);
        User user = templateRepository.findByUserId(userId);
        log.info("locList is null : {}", user.getLocList() == null);
        if (rollback) {
            throw new RuntimeException("userId == " + userId + " is not update.");
        }
    }

    @Transactional
    public void multiDocumentTransactionTest(Long userId, boolean rollback) {
        User user = templateRepository.findByUserId(userId);
        user.setLocList(null);
        UserDetail userDetail = userDetailRepository.findByDetailId(userId);
        userDetail.setLoc(null);
        userRepository.save(user);
        userDetailRepository.save(userDetail);
        if (rollback) {
            throw new RuntimeException("userId == " + userId + " is not update.");
        }
    }

    @Transactional(readOnly = true)
    public void readOnlyTest(Long userId) {
        userRepository.findByUserId(userId);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void userUpdate(Long userId) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("==> start user locList null update");
        templateRepository.updateLocByUserId(userId, null);
        log.info("==> end user locList null update");
    }

    @Transactional
    public void writeConflictTest(Long userId) {
        log.info("==> start user locList update");
        userRepository.findByUserId(userId);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        templateRepository.updateLocByUserId(userId, new String[]{"seoul1", "busan1"});
        log.info("==> end user locList update");
    }
}
