package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.dto.UserDto;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.user.UserInitRepositoryTest;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.config.location=classpath:application-local.properties"})
@Log4j2
public class UserDtoTest extends UserInitRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Test
    public void user_mapper_test() throws Exception {
        //given
        long userId = 1L;
        User userByRepository = userRepository.findByUserId(userId);
        //when
        UserDto userDto = modelMapper.map(userByRepository, UserDto.class);
        //done
        Assertions.assertThat(userByRepository.getUserId()).isEqualTo(userDto.getUserId());
    }
}
