package com.chheang.mengheak.solid;

import com.chheang.mengheak.solid.common.EmailService;
import com.chheang.mengheak.solid.common.InMemoryUserRepository;
import com.chheang.mengheak.solid.common.ReportService;
import com.chheang.mengheak.solid.common.UserRegisterRequest;
import com.chheang.mengheak.solid.s_srp.good.NewUserReportGenerator;
import com.chheang.mengheak.solid.s_srp.good.UserMapper;
import com.chheang.mengheak.solid.s_srp.good.UserRegisterValidator;
import com.chheang.mengheak.solid.s_srp.good.UserService;
import com.chheang.mengheak.solid.s_srp.good.UserWriter;
import com.chheang.mengheak.solid.s_srp.good.WelcomeEmailSender;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService(
                new UserRegisterValidator(),
                new UserMapper(),
                new UserWriter(new InMemoryUserRepository()),
                new WelcomeEmailSender(new EmailService()),
                new NewUserReportGenerator(new ReportService())
        );

        userService.register(new UserRegisterRequest("Piseth", "piseth@example.com"));
    }
}
