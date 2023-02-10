package com.me.springdata.mongodb.service;

import com.me.springdata.mongodb.document.User;
import com.me.springdata.mongodb.dto.UserDto;
import com.me.springdata.mongodb.repository.user.UserRepository;
import com.me.springdata.mongodb.response.ApiCommonResponse;
import com.me.springdata.mongodb.response.UserListResponse;
import com.me.springdata.mongodb.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserListResponse findUserList() {
        List<User> all = userRepository.findAll();
        return UserListResponse.builder().users(all).build();
    }

    public UserResponse findUserByUserId(Long userId) {
        return UserResponse.builder().user(userRepository.findByUserId(userId)).build();
    }

    public ApiCommonResponse saveUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
        return new ApiCommonResponse();
    }
}
