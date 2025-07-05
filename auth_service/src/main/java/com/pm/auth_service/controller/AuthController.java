package com.pm.auth_service.controller;

import com.pm.auth_service.dto.ProfileDto;
import com.pm.auth_service.dto.RegisterDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {

    @PostMapping("")
    public ProfileDto postUser(RegisterDto registerDto) {
        return null;
    }
}
