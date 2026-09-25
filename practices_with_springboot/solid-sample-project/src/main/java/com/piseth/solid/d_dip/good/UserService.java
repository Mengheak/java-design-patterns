package com.piseth.solid.d_dip.good;

import com.piseth.solid.common.User;
import com.piseth.solid.common.UserRegisterRequest;

/**
 * GOOD EXAMPLE: High-level class depends on abstraction.
 */
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(UserRegisterRequest request) {
        User user = new User(request.getName(), request.getEmail(), "ACTIVE");
        return repository.save(user);
    }
}
