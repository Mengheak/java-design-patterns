package com.piseth.solid.common;

import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String email;
    private String status;

    public User(String name, String email, String status) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
