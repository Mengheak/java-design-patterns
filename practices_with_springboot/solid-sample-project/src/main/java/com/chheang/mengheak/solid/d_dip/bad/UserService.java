package com.chheang.mengheak.solid.d_dip.bad;

import com.chheang.mengheak.solid.common.User;
import com.chheang.mengheak.solid.common.UserRegisterRequest;

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
