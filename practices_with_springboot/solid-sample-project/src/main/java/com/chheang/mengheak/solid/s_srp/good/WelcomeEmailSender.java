package com.chheang.mengheak.solid.s_srp.good;

import com.chheang.mengheak.solid.common.EmailService;
import com.chheang.mengheak.solid.common.User;

public class WelcomeEmailSender {

    private final EmailService emailService;

    public WelcomeEmailSender(EmailService emailService) {
        this.emailService = emailService;
    }

    public void send(User user) {
        emailService.send(user.getEmail(), "Welcome", "Hello " + user.getName());
    }
}
