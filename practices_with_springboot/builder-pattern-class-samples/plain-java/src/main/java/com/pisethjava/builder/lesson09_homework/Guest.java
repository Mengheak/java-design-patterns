package com.pisethjava.builder.lesson09_homework;

import java.util.UUID;

public final class Guest {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String email;
    private final String nationality;
    private final String passportNumber;
    private final boolean vip;

    private Guest(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phone = builder.phone;
        this.email = builder.email;
        this.nationality = builder.nationality;
        this.passportNumber = builder.passportNumber;
        this.vip = builder.vip;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;
        private String nationality;
        private String passportNumber;
        private boolean vip;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder nationality(String nationality) { this.nationality = nationality; return this; }
        public Builder passportNumber(String passportNumber) { this.passportNumber = passportNumber; return this; }
        public Builder vip(boolean vip) { this.vip = vip; return this; }

        public Guest build() {
            if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("First name is required");
            if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Last name is required");
            if (email == null || email.isBlank()) throw new IllegalArgumentException("Email is required");
            return new Guest(this);
        }
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", nationality='" + nationality + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", vip=" + vip +
                '}';
    }
}
