package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import com.me.springdata.mongodb.user.UserInitRepositoryTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest(properties = {"spring.config.location=classpath:application-local-cloud.properties"})
public class UserRollbackTest extends UserInitRepositoryTest {
    @Autowired
    UserTransactionService userTransactionService;

    @Autowired
    UserService userService;
    @Autowired
    UserTemplateRepository userTemplateRepository;
    @Autowired
    UserDetailRepository userDetailRepository;

    @Test
    public void user_rollback_template_test() {
        //given
        Long userId = 1L;
        Long userId2 = 2L;

        //when
        userTransactionService.singleTransactionTest(userId, false);
        User user = userTemplateRepository.findByUserId(userId);

        Assertions.assertThrowsExactly(RuntimeException.class, () -> userTransactionService.singleTransactionTest(userId2, true));
        User user2 = userTemplateRepository.findByUserId(userId2);

        //done
        Assertions.assertNull(user.getLocList());
        Assertions.assertNotNull(user2.getLocList());
    }

    @Test
    public void multiple_transaction_test() {
        //given
        Long userId = 1L;
        Long userId2 = 2L;

        //when
        userTransactionService.multiDocumentTransactionTest(userId, false);
        Assertions.assertThrowsExactly(RuntimeException.class, () -> userTransactionService.multiDocumentTransactionTest(userId2,true));
        User user2 = userTemplateRepository.findByUserId(userId2);
        UserDetail userDetail = userDetailRepository.findByUserId(userId2);

        //done
        Assertions.assertNotNull(user2.getLocList());
        Assertions.assertNotNull(userDetail.getLoc());
    }
}
