package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import com.me.springdata.mongodb.user.UserInitRepositoryTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest(properties = {
        "spring.config.location=classpath:application-local-cloud.properties"})
public class UserTransactionTest extends UserInitRepositoryTest {

    @Autowired
    UserTransactionService userTransactionService;
    @Autowired
    UserTemplateRepository userTemplateRepository;
    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    UserTotalTransactionService userTotalTransactionService;

    @Test
    @DisplayName("첫번째 transaction 요청에서 새로운 transaction을 생성하고 db 작업이 commit이 되고, exception이 발생하면 commit된 작업은 제외하고 롤백된다.")
    public void required_new_test() {
        //given
        Long userId1 = 1L;
        Long userId2 = 2L;

        //when
        log.info("userId : 1 transaction.");
        userTotalTransactionService.requiredNewTest(userId1, false);
        log.info("userId : 2 transaction.");
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> userTotalTransactionService.requiredNewTest(userId2, true)).printStackTrace();

        //done
        User user1 = userTemplateRepository.findByUserId(userId1);
        UserDetail userDetail1 = userDetailRepository.findByUserId(userId1);

        User user2 = userTemplateRepository.findByUserId(userId2);
        UserDetail userDetail2 = userDetailRepository.findByUserId(userId2);

        Assertions.assertNull(user1.getLocList());
        Assertions.assertNull(userDetail1.getLoc());

        Assertions.assertNotNull(user2.getLocList());
        Assertions.assertNull(userDetail2.getLoc());
    }

    @Test
    @DisplayName("transactional 요청에서 새로운 transaction을 생성하고 생성된 transaction에서 unchecked exception이 발생할 경우 exception이 전파되어 모두 롤백된다.")
    public void required_new_test2() {
        //given
        Long userId1 = 1L;

        //when
        log.info("userId : 1 transaction.");
        Assertions.assertThrowsExactly(RuntimeException.class,
                        () -> userTotalTransactionService.requiredNewRollbackTest(userId1, true))
                .printStackTrace();

        //done
        User user = userTemplateRepository.findByUserId(userId1);
        UserDetail userDetail = userDetailRepository.findByUserId(userId1);
        Assertions.assertNotNull(user.getLocList());
        Assertions.assertNotNull(userDetail.getLoc());
    }

    @Test
    @DisplayName("readOnly test, spring data mongodb 에서는 persistence context를 지원하지 않기 때문에 동작 하지 않음")
    public void read_only_test() {
        //given
        Long userId = 1L;

        //when
        userTotalTransactionService.readOnlyTest(userId);

        //done
        User user = userTemplateRepository.findByUserId(userId);
        Assertions.assertNull(user.getLocList());
    }

    @Test
    @DisplayName("두개의 트랜잭션에서 같은 document를 수정하려고 했을 경우 WriteConflict 에러가 발생함")
    public void write_conflict_test() {
        //given
        Long userId = 1L;

        //when
        Assertions.assertThrowsExactly(RuntimeException.class,
                () -> userTotalTransactionService.WriteConflictTest(userId)).printStackTrace();

        //done
        User user = userTemplateRepository.findByUserId(userId);
        Assertions.assertNull(user.getLocList());
    }
}
