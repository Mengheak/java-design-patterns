package com.piseth.solid.s_srp.good;

import com.piseth.solid.common.User;
import com.piseth.solid.common.UserRegisterRequest;

/**
 * GOOD EXAMPLE: This class coordinates the registration flow only.
 */
public class UserService {

    private final UserRegisterValidator validator;
    private final UserMapper userMapper;
    private final UserWriter userWriter;
    private final WelcomeEmailSender welcomeEmailSender;
    private final NewUserReportGenerator reportGenerator;

    public UserService(
            UserRegisterValidator validator,
            UserMapper userMapper,
            UserWriter userWriter,
            WelcomeEmailSender welcomeEmailSender,
            NewUserReportGenerator reportGenerator
    ) {
        this.validator = validator;
        this.userMapper = userMapper;
        this.userWriter = userWriter;
        this.welcomeEmailSender = welcomeEmailSender;
        this.reportGenerator = reportGenerator;
    }

    public void register(UserRegisterRequest request) {
        System.out.println("Start registering user: " + request.getEmail());

        validator.validate(request);
        User user = userMapper.toEntity(request);
        User savedUser = userWriter.save(user);
        welcomeEmailSender.send(savedUser);
        reportGenerator.generate(savedUser);

        System.out.println("Finished registering user: " + savedUser.getId());
    }
}
