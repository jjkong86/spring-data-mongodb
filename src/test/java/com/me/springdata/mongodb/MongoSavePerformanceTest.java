package com.me.springdata.mongodb;

import com.me.springdata.mongodb.document.Address;
import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.user.template.UserTemplateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(properties = {"spring.config.location=classpath:application-local.properties"})
public class MongoSavePerformanceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserTemplateRepository userTemplateRepository;

    @Test
    public void save_performance_test() {
        //given
        userRepository.deleteAll();

        //when
        for (long i = 0; i < 1000; i++) {
            userRepository.insert(User.builder().userId(i).firstName("Joe").age(12321).locList(new String[]{"seoul", "busan"}).userDetail(UserDetail.builder().detailId(1L).hp("0104923").loc("seoul").build()).createTime(LocalDateTime.now()).address(Address.builder().addressId(1L).loc("busan").build()).build());
        }
        List<User> users = userRepository.findAll();

        //done
        String[] locList = {"seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1", "seoul1", "busan1"};
        long time = System.currentTimeMillis();
        for (User user : users) {
            user.setLocList(locList);
            userRepository.save(user);
        }
        System.out.println("jpa performance : " + (System.currentTimeMillis() - time));

        time = System.currentTimeMillis();
        for (User user : users) {
            userTemplateRepository.updateLocByUserId(user.getUserId(), locList);
        }
        System.out.println("template performance : " + (System.currentTimeMillis() - time));
    }
}
