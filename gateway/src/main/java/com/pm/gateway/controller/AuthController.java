package com.pm.gateway.controller;

import com.pm.authservice.grpc.*;
import com.pm.gateway.dto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @GrpcClient("authService")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto dto) {

        LoginRequest req = LoginRequest.newBuilder()
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .build();

        LoginResponse res  = authServiceStub.login(req);

        return ResponseEntity.ok(new LoginResponseDto(res.getStatus(), res.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<BooleanDto> register(@RequestBody RegisterDto dto) {

        RegisterRequest req = RegisterRequest.newBuilder()
                .setFullname(dto.getFullname())
                .setEmail(dto.getEmail())
                .setPassword(dto.getPassword())
                .setRole(dto.getRole())
                .build();

        BooleanResponse res = authServiceStub.register(req);

        return ResponseEntity.ok(new BooleanDto(res.getStatus()));
    }
}
