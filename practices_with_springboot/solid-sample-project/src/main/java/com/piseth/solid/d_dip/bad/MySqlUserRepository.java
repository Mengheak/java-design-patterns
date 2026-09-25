package com.piseth.solid.d_dip.bad;

import com.piseth.solid.common.User;

public class MySqlUserRepository {

    public User save(User user) {
        System.out.println("Saved user using MySQL repository");
        return user;
    }
}
