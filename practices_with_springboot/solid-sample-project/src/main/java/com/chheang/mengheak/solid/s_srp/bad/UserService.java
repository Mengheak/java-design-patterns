package com.chheang.mengheak.solid.s_srp.bad;

import com.chheang.mengheak.solid.common.EmailService;
import com.chheang.mengheak.solid.common.ReportService;
import com.chheang.mengheak.solid.common.User;
import com.chheang.mengheak.solid.common.UserRegisterRequest;
import com.chheang.mengheak.solid.common.UserRepository;

/**
 * BAD EXAMPLE: This class violates SRP.
 * It validates, saves data, sends email, logs, and generates report.
 */
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ReportService reportService;

    public UserService(UserRepository userRepository, EmailService emailService, ReportService reportService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.reportService = reportService;
    }

    public void register(UserRegisterRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!request.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        System.out.println("Start registering user: " + request.getEmail());

        User user = new User(request.getName(), request.getEmail(), "ACTIVE");
        User savedUser = userRepository.save(user);

        emailService.send(
                savedUser.getEmail(),
                "Welcome",
                "Hello " + savedUser.getName()
        );

        reportService.generateNewUserReport(savedUser);

        System.out.println("Finished registering user: " + savedUser.getId());
    }
}
