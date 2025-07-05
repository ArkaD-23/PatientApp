package com.pm.auth_service.controller;

import com.pm.auth_service.dto.BooleanDto;
import com.pm.auth_service.dto.LoginDto;
import com.pm.auth_service.dto.ProfileDto;
import com.pm.auth_service.dto.RegisterDto;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.auth_service.exception.UserAlreadyLoggedInException;
import com.pm.auth_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<BooleanDto> postUser(
            Authentication authentication,
            RegisterDto registerDto
    ) {

        try {

            BooleanDto status = authService.registerUser(authentication, registerDto);
            return ResponseEntity.ok().body(status);
        } catch (UserAlreadyLoggedInException | UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BooleanDto(false));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<BooleanDto> loginUser(
            Authentication authentication,
            LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        try {

            BooleanDto status = authService.userLogin(authentication, loginDto, request, response);
            return ResponseEntity.ok().body(status);
        } catch (UserAlreadyLoggedInException userAlreadyLoggedInException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BooleanDto(false));
        }

    }
}
