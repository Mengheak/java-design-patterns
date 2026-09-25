package com.piseth.solid.d_dip.bad;

import com.piseth.solid.common.User;
import com.piseth.solid.common.UserRegisterRequest;

/**
 * BAD EXAMPLE: High-level class depends directly on a concrete repository.
 */
public class UserService {

    private final MySqlUserRepository repository = new MySqlUserRepository();

    public User register(UserRegisterRequest request) {
        User user = new User(request.getName(), request.getEmail(), "ACTIVE");
        return repository.save(user);
    }
}
