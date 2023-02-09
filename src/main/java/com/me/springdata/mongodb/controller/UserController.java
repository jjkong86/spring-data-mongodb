package com.me.springdata.mongodb.controller;

import com.me.springdata.mongodb.dto.UserDto;
import com.me.springdata.mongodb.response.ApiCommonResponse;
import com.me.springdata.mongodb.response.UserListResponse;
import com.me.springdata.mongodb.response.UserResponse;
import com.me.springdata.mongodb.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/users")
    public UserListResponse getUserList() {
        return userService.findUserList();
    }

    @GetMapping(value = "/users/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.findUserByUserId(userId);
    }

    @PostMapping(value = "/users")
    public ApiCommonResponse saveUser(@RequestBody UserDto user) {
        return userService.saveUser(user);
    }
}
