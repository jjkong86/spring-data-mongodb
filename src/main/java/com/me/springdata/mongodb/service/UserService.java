package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.document.UserDetail;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.repository.userdetail.UserDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserDetailRepository userDetailRepository;
    MongoTemplate mongoTemplate;

    @Transactional
    public void transactionTest() {
        startJob();
        userRepository.save(User.builder().userId(1L).firstName("Joe").age(12321).locList(new String[]{"seoul", "busan"}).build());
    }

    public void startJob() {
        userDetailRepository.save(UserDetail.builder().userId(1L).hp("0104923").loc("seoul").build());
        userDetailRepository.save(UserDetail.builder().userId(2L).hp("0102222").loc("seoul").build());
        userDetailRepository.save(UserDetail.builder().userId(3L).hp("0103333").loc("seoul").build());
        userDetailRepository.save(UserDetail.builder().userId(4L).hp("0104444").loc("seoul44").build());

        userRepository.insert(User.builder().userId(1L).firstName("Joe").age(12321).locList(new String[]{"seoul", "busan"}).userDetail(UserDetail.builder().userId(1L).hp("0104923").loc("seoul").build()).build());
        userRepository.insert(User.builder().userId(2L).firstName("Joe231").age(123321).locList(new String[]{"seoul3", "busan"}).userDetail(UserDetail.builder().userId(2L).hp("0102222").loc("seoul").build()).build());
        mongoTemplate.insert(User.builder().userId(3L).firstName("Joe33").age(33).locList(new String[]{"seoul4", "busan"}).userDetail(UserDetail.builder().userId(3L).hp("0103333").loc("seoul").build()).build());
        userRepository.insert(User.builder().userId(4L).firstName("Joe").age(1).locList(new String[]{"seoul2", "busan"}).userDetail(UserDetail.builder().userId(4L).hp("0104444").loc("seoul44").build()).build());
    }

    public void endJob() {
        userRepository.deleteAll();
        userDetailRepository.deleteAll();
    }
}
