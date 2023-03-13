package com.me.springdata.mongodb.user;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Log4j2
@SpringBootTest(properties = {"spring.config.location=classpath:application-local.properties"})
//@Transactional
public class UserReferenceRepositoryTestTest extends UserInitRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailRepository userDetailRepository;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @DisplayName("user, userDetail reference lazy test")
    @Test
    public void user_reference_lazy_test() throws InterruptedException {
        //given
        long userId = 1L;
        //when
        User user = userRepository.findByUserId(userId);

        //done
        Thread.sleep(1000);
        log.info(user.getUserDetail());
    }

    @DisplayName("user, userDetail reference field injection")
    @Test
    public void user_reference_field_injection_test() throws InterruptedException {
        //given
        long userId = 1L;
        //when
        User user = userRepository.findByUserId(userId);
        UserDetail userDetail = userDetailRepository.findByDetailId(userId);
        user.setUserDetail(userDetail);
        //done
        Thread.sleep(1000);
        UserDetail userDetail1 = user.getUserDetail();
        log.info(userDetail1);
    }

    @DisplayName("list user, userDetail reference field injection")
    @Test
    public void list_user_reference_field_injection_test() {
        //when
        List<User> findAllUser = userRepository.findAll();
        List<Long> userIdList = findAllUser.stream().map(User::getUserId).toList();
        List<UserDetail> findUserDetailList = userDetailRepository.findByDetailIdIn(userIdList);
        Map<Long, UserDetail> userDetailMap = findUserDetailList.stream().collect(Collectors.toMap(UserDetail::getDetailId, Function.identity()));
        findAllUser.forEach(user -> user.setUserDetail(userDetailMap.get(user.getUserId())));

        //done
        findAllUser.forEach(user -> log.info(user.getUserDetail()));
    }

    @DisplayName("user, userDetail reference test")
    @Test
    public void user_reference_test() {
        //given
        long userId = 1L;
        //when
        userRepository.findByUserId(userId);

        //done
//        log.info(userByUserId.getUserDetail());
    }


    @DisplayName("findAll -> user, userDetail reference lazy test, n+1 주의")
    @Test
    public void user_reference_lazy_N_plus_1_test() {
        //given
        //when
        List<User> all = userRepository.findAll();
        //done
        all.forEach(user -> {
            UserDetail userDetail = user.getUserDetail();
            log.info("userDetail : {}", userDetail);
            log.info(user);
        });
    }

    @Test
    public void timezone_test() {
        //given
        long userId = 1L;
        long userId2 = 2L;
        //when
        User userByUserId = userRepository.findByUserId(userId);
        User userByUserId2 = userRepository.findByUserId(userId2);

        LocalDateTime now = LocalDateTime.now();
        log.info("Asia/Seoul > {}", now);
        userByUserId2.setCreateTime(now);
        userRepository.save(userByUserId2);

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Taipei"));
        LocalDateTime taipei = LocalDateTime.now();
        log.info("Asia/Taipei > {}", taipei);
        userByUserId.setCreateTime(taipei);
        userRepository.save(userByUserId);

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        //done
        userByUserId = userRepository.findByUserId(userId);
        userByUserId2 = userRepository.findByUserId(userId2);

        System.out.println("utc Asia/Taipei : " + userByUserId.getCreateTime());
        System.out.println("utc Asia/Seoul : " + userByUserId2.getCreateTime());
    }
}
