package com.pm.chat_service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "Service is alive!";
    }
}
