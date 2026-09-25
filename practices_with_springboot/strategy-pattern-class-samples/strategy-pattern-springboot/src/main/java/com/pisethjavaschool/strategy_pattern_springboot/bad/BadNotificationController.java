package com.pisethjavaschool.strategy_pattern_springboot.bad;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bad/notifications")
public class BadNotificationController {

    private final BadNotificationService service;

    public BadNotificationController(BadNotificationService service) {
        this.service = service;
    }

    @GetMapping("/send")
    public String send(
            @RequestParam String type,
            @RequestParam String message
    ) {
        return service.send(type, message);
    }
}
