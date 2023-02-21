package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserTemplateService {
    private final UserTemplateRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    public long userUpdate(Long userId) {
        long l = repository.updateLocByUserId(userId, null);
        User user = repository.findByUserId(userId);
        if (user.getUserId() == 2L) {
            throw new RuntimeException("userId == "+userId+" is not update.");
        }

        return l;
    }
}
