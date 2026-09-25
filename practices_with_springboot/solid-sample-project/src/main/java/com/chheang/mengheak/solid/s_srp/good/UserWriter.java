package com.chheang.mengheak.solid.s_srp.good;

import com.chheang.mengheak.solid.common.User;
import com.chheang.mengheak.solid.common.UserRepository;

public class UserWriter {

    private final UserRepository userRepository;

    public UserWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
