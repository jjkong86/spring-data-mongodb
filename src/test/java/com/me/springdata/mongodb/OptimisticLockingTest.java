package com.me.springdata.mongodb;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.user.UserInitRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

@SpringBootTest(properties = {"spring.config.location=classpath:application-local.properties"})
public class OptimisticLockingTest extends UserInitRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @Test
    public void optimistic_locking_jpa_test() {
        //given
        long userId = 4L;

        //when
        User updatableUser = userRepository.findByUserId(userId);
        User findUser = userRepository.findByUserId(userId);
        updatableUser.setAge(18);
        userRepository.save(updatableUser);

        //done
        Assertions.assertThrowsExactly(OptimisticLockingFailureException.class, () -> userRepository.save(findUser)).printStackTrace();
    }

    @Test
    public void optimistic_locking_template_save_test() {
        //given
        long userId = 4L;

        //when
        User updatableUser = userTemplateRepository.findByUserId(userId);
        User findUser = userTemplateRepository.findByUserId(userId);
        updatableUser.setAge(18);
        userTemplateRepository.save(updatableUser);

        //done
        Assertions.assertThrowsExactly(OptimisticLockingFailureException.class, () -> userTemplateRepository.save(findUser)).printStackTrace();
    }

    @Test
    public void optimistic_locking_template_update_test() {
        //given
        long userId = 4L;

        //when
        User updatableUser = userTemplateRepository.findByUserId(userId);
        User findUser = userTemplateRepository.findByUserId(userId);
        updatableUser.setAge(18);
        userTemplateRepository.updateAgeByUserId(userId, updatableUser.getAge());

        //done
        Assertions.assertEquals(userTemplateRepository.updateAgeByUserId(userId, findUser.getAge()), 1);
    }
}
