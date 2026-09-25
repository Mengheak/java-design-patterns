package com.chheang.mengheak.solid.d_dip.good;

import com.chheang.mengheak.solid.common.User;

public class PostgresUserRepository implements UserRepository {

    @Override
    public User save(User user) {
        System.out.println("Saved user using PostgreSQL repository");
        return user;
    }
}
