package com.piseth.solid.s_srp.good;

import com.piseth.solid.common.User;
import com.piseth.solid.common.UserRepository;

public class UserWriter {

    private final UserRepository userRepository;

    public UserWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
