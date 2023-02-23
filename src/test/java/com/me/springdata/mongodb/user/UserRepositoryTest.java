package com.me.springdata.mongodb.user;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SpringBootTest(properties = {"spring.config.location=classpath:application-local.properties"})
//@Transactional
@Log4j2
public class UserRepositoryTest extends UserInitRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailRepository userDetailRepository;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @DisplayName("findByUserId jpa와 template 결과 비교")
    @Test
    public void findByUserId_compare_jpa_template() {
        //given
        long userId = 1L;
        //when
        User userByRepository = userRepository.findByUserId(userId);
        User userByTemplate = userTemplateRepository.findByUserId(userId);
        //done
        Assertions.assertThat(userByTemplate.getUserId()).isEqualTo(userByRepository.getUserId());
    }

    @DisplayName("update test, jpa와 template 결과 비교")
    @Test
    public void updateTest_compare_jap_template() {
        //given
        long userId = 1L;
        long userId2 = 2L;
        //when
        //update
        User userByRepository = userRepository.findByUserId(userId);
        String[] locList = {"a", "b", "c"};
        userByRepository.setLocList(locList);
        userRepository.save(userByRepository);
        userTemplateRepository.updateLocByUserId(userId2, locList);

        // update after find
        userByRepository = userRepository.findByUserId(userId);
        User userByTemplate = userTemplateRepository.findByUserId(userId2);

        //done
        Assertions.assertThat(userByRepository.getLocList()).containsExactly(userByTemplate.getLocList());
    }


    @DisplayName("delete test")
    @Test
    public void delete_compare_jpa_template() {
        //given
        long userId = 1L;
        long userId2 = 2L;
        //when
        long deleteByRepository = userRepository.deleteByUserId(userId);
        long deleteByTemplate = userTemplateRepository.deleteByUserId(userId2);
        //done
        Assertions.assertThat(deleteByRepository).isEqualTo(deleteByTemplate);
    }

    // https://lankydan.dev/2017/05/29/embedded-documents-with-spring-data-and-mongodb
    @Test
    public void user_embedded() {
        //given
        Long addressId = 1L;
        //when
        Optional<List<User>> user = userRepository.findByAddressId(addressId);
        //done
        log.info(user);
    }

    @Test
    public void user_jpa_template_code_compare() {
        //given
        ArrayList<Long> userIdList = new ArrayList<>(Arrays.asList(1L, 2L));
        //when
        List<User> usersByRepository = userRepository.findTop3ByUserIdInOrderByFirstNameDescAgeAsc(userIdList);
        List<User> usersByTemplate = userTemplateRepository.findTop3ByUserIdInOrderByFirstNameDescAgeAsc(userIdList, 3);
        //done
        usersByRepository.forEach(user -> log.info(user.getUserId()));
        log.info("=============================================");
        usersByTemplate.forEach(user -> log.info(user.getUserId()));
    }

    @Test
    public void user_jpa_template_async_test() {
        //given
        String joe = "Joe";
        //when
        CompletableFuture<List<User>> resultByRepository = userRepository.findByFirstNameOrderByFirstNameAsc(joe);
        CompletableFuture<List<User>> resultByTemplate = userTemplateRepository.findByFirstNameOrderByFirstNameAsc(joe);
        log.info("=====before======");
        //done
        resultByRepository.thenAccept(users -> users.forEach(user -> log.info(user.getUserId())));
        resultByTemplate.thenAccept(users -> users.forEach(user -> log.info(user.getUserId())));
    }

    @Test
    public void user_jap_element_match_test() {
        //given
        String loc = "busan";
        //when
        List<User> users = userRepository.findByElementMatch(loc);
        //done

        Assertions.assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    public void user_jpa_update_test() {
        //given
        Long userId = 1L;
        //when
        long result = userRepository.updateByUserIdSetAge(1L, 999);

        //done
        Assertions.assertThat(result).isPositive();
    }
}
