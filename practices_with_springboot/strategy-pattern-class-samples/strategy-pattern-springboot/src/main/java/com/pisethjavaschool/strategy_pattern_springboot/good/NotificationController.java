package com.pisethjavaschool.strategy_pattern_springboot.good;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/good/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
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