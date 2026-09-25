package com.chheang.mengheak.solid.s_srp.good;

import com.chheang.mengheak.solid.common.User;
import com.chheang.mengheak.solid.common.UserRegisterRequest;

public class UserMapper {

    public User toEntity(UserRegisterRequest request) {
        return new User(request.getName(), request.getEmail(), "ACTIVE");
    }
}
