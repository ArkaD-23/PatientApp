package com.pm.auth_service.controller;

import com.pm.auth_service.dto.*;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<BooleanDto> register(@RequestBody RegisterDto registerDto) {
        try {
            BooleanDto result = authService.registerUser(registerDto);
            System.out.println(result);
            return ResponseEntity.ok(result);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new BooleanDto(false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BooleanDto(false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        try {
            LoginResponseDto result = authService.userLogin(loginDto);
            System.out.println(result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(false, null));
        }
    }
}
