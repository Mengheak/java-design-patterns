package com.piseth.solid.common;

public class UserRegisterRequest {

    private String name;
    private String email;

    public UserRegisterRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
