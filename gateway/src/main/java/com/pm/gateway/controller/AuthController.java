package com.pm.gateway.controller;

import com.pm.gateway.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {

        System.out.println("Dto: " + loginDto);
        try {
            HttpEntity<LoginDto> request = new HttpEntity<>(loginDto);
            ResponseEntity<LoginResponseDto> response = restTemplate.exchange(
                    authServiceUrl + "/login",
                    HttpMethod.POST,
                    request,
                    LoginResponseDto.class
            );
            System.out.println("res: " + response);
            if(response != null && response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<BooleanDto> register(@RequestBody RegisterDto dto) {

        System.out.println("Dto: " + dto);
        try {
            HttpEntity<RegisterDto> request = new HttpEntity<>(dto);
            ResponseEntity<BooleanDto> response = restTemplate.exchange(
                    authServiceUrl + "/register",
                    HttpMethod.POST,
                    request,
                    BooleanDto.class
            );
            System.out.println("res: " + response);
            if(response != null && response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
