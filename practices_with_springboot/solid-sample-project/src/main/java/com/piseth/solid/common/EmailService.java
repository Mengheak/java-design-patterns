package com.piseth.solid.common;

public class EmailService {

    public void send(String to, String subject, String body) {
        System.out.println("Sending email to " + to + ": " + subject);
    }
}
