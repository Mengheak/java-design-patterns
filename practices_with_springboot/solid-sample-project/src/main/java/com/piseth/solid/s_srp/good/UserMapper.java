package com.piseth.solid.s_srp.good;

import com.piseth.solid.common.User;
import com.piseth.solid.common.UserRegisterRequest;

public class UserMapper {

    public User toEntity(UserRegisterRequest request) {
        return new User(request.getName(), request.getEmail(), "ACTIVE");
    }
}
