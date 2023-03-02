package com.me.springdata.mongodb;

import com.me.springdata.mongodb.document.Address;
import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.user.UserInitRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;

@SpringBootTest(properties = {"spring.config.location=classpath:application-local.properties"})
public class OptimisticLockingTest extends UserInitRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @Test
    public void optimistic_locking_jpa_test() {
        //given
        long userId = 5L;
        User user = User.builder().userId(userId).firstName("Joe5").age(12321).locList(new String[]{"seoul5", "busan5"}).userDetail(UserDetail.builder().userId(1L).hp("0104923").loc("seoul").build()).createTime(LocalDateTime.now()).address(Address.builder().addressId(1L).loc("busan").build()).build();
        userRepository.save(user);

        //when
        User findUser = userRepository.findByUserId(userId);
        user.setAge(123456);
        userRepository.save(user);

        //done
        Assertions.assertThrowsExactly(OptimisticLockingFailureException.class, () -> userRepository.save(findUser)).printStackTrace();
    }

    @Test
    public void optimistic_locking_template_test() {
        //given
        long userId = 5L;
        User user = User.builder().userId(userId).firstName("Joe5").age(12321).locList(new String[]{"seoul5", "busan5"}).userDetail(UserDetail.builder().userId(1L).hp("0104923").loc("seoul").build()).createTime(LocalDateTime.now()).address(Address.builder().addressId(1L).loc("busan").build()).build();
        userTemplateRepository.save(user);

        //when
        User findUser = userTemplateRepository.findByUserId(userId);
        user.setAge(123456);
        userTemplateRepository.save(user);

        //done
        Assertions.assertThrowsExactly(OptimisticLockingFailureException.class, () -> userTemplateRepository.save(findUser)).printStackTrace();
    }
}
