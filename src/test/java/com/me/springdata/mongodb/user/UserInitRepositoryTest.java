package com.me.springdata.mongodb.user;

import com.me.springdata.mongodb.document.Address;
import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"})
//@Transactional
public class UserInitRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailRepository userDetailRepository;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @BeforeEach
    public void beforeClass() {
        userDetailRepository.deleteAll();
        userRepository.deleteAll();

        userDetailRepository.save(UserDetail.builder().userId(1L).hp("0104923").loc("seoul").createTime(LocalDateTime.now()).build());
        userDetailRepository.save(UserDetail.builder().userId(2L).hp("0102222").loc("seoul").createTime(LocalDateTime.now()).build());
        userDetailRepository.save(UserDetail.builder().userId(3L).hp("0103333").loc("seoul").createTime(LocalDateTime.now()).build());
        userDetailRepository.save(UserDetail.builder().userId(4L).hp("0104444").loc("seoul44").createTime(LocalDateTime.now()).build());

        userRepository.insert(User.builder().userId(1L).firstName("Joe").age(12321).locList(new String[]{"seoul", "busan"}).userDetail(UserDetail.builder().userId(1L).hp("0104923").loc("seoul").build()).createTime(LocalDateTime.now()).address(Address.builder().addressId(1L).loc("busan").build()).build());
        userRepository.insert(User.builder().userId(2L).firstName("Joe231").age(123321).locList(new String[]{"seoul3", "busan"}).userDetail(UserDetail.builder().userId(2L).hp("0102222").loc("seoul").build()).createTime(LocalDateTime.now()).build());
        userRepository.insert(User.builder().userId(4L).firstName("Joe").age(1).locList(new String[]{"seoul2", "busan"}).userDetail(UserDetail.builder().userId(4L).hp("0104444").loc("seoul44").build()).createTime(LocalDateTime.now()).build());
        userTemplateRepository.insert(User.builder().userId(3L).firstName("Joe33").age(33).locList(new String[]{"seoul4", "busan"}).userDetail(UserDetail.builder().userId(3L).hp("0103333").loc("seoul").build()).createTime(LocalDateTime.now()).build());
    }
}
