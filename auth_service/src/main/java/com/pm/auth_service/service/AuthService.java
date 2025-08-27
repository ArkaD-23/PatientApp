package com.pm.auth_service.service;

import com.pm.auth_service.dto.*;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}")
    private String userServiceUrl;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public BooleanDto registerUser(RegisterDto registerDto) {
        try {
            HttpEntity<RegisterDto> request = new HttpEntity<>(registerDto);
            ResponseEntity<BooleanDto> response = restTemplate.exchange(
                    userServiceUrl + "/create",
                    HttpMethod.POST,
                    request,
                    BooleanDto.class
            );

            if (response.getBody() != null && response.getBody().getStatus()) {
                return new BooleanDto(true);
            } else {
                throw new UserAlreadyExistsException("User already exists");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to register user: " + e.getMessage());
        }
    }

    public LoginResponseDto userLogin(LoginDto loginDto) {
        try {
            HttpEntity<LoginDto> request = new HttpEntity<>(loginDto);
            ResponseEntity<BooleanDto> response = restTemplate.exchange(
                    userServiceUrl + "/validate",
                    HttpMethod.POST,
                    request,
                    BooleanDto.class
            );

            if (response.getBody() != null && response.getBody().getStatus()) {
                String jwt = jwtUtil.generateToken(loginDto.getEmail());
                return new LoginResponseDto(true, jwt);
            } else {
                return new LoginResponseDto(false, "");
            }

        } catch (Exception e) {
            return new LoginResponseDto(false, "");
        }
    }
}
