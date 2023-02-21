package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.user.UserInitRepositoryTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@Log4j2
@SpringBootTest(properties = {"spring.config.location=classpath:application-local-cloud.properties"})
public class UserRollbackTest extends UserInitRepositoryTest {
    @Autowired
    UserTemplateService userTemplateService;

    @Autowired
    UserService userService;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    public void user_rollback_template_test() {
        //given
        Long userId = 1L;
        Long userId2 = 2L;

        //when
        userTemplateService.userUpdate(userId);
        User user = userTemplateRepository.findByUserId(userId);

        Assertions.assertThrowsExactly(RuntimeException.class, () -> userTemplateService.userUpdate(userId2));
        User user2 = userTemplateRepository.findByUserId(userId2);

        //done
        Assertions.assertNull(user.getLocList());
        Assertions.assertNotNull(user2.getLocList());
    }
}
