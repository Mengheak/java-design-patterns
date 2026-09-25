package com.piseth.solid.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> database = new HashMap<>();

    @Override
    public User save(User user) {
        database.put(user.getId(), user);
        System.out.println("Saved user to database: " + user.getEmail());
        return user;
    }
}
