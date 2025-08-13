package com.pm.auth_service.service;

import com.pm.auth_service.dto.*;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.userservice.grpc.BooleanResponse;
import com.pm.userservice.grpc.CreateUserRequest;
import com.pm.userservice.grpc.UserServiceGrpc;
import com.pm.userservice.grpc.ValidateUserRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @GrpcClient("userService")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public BooleanDto registerUser(RegisterDto registerDto) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setEmail(registerDto.getEmail())
                .setPassword(registerDto.getPassword())
                .setFullname(registerDto.getFullname())
                .setRole(registerDto.getRole())
                .build();

        BooleanResponse response = userStub.createUser(request);

        if (!response.getStatus()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        return new BooleanDto(true);
    }

    public LoginResponseDto userLogin(LoginDto loginDto) {
        ValidateUserRequest request = ValidateUserRequest.newBuilder()
                .setEmail(loginDto.getEmail())
                .setPassword(loginDto.getPassword())
                .build();

        BooleanResponse response = userStub.validateUser(request);

        if (!response.getStatus()) {
            return new LoginResponseDto(false, "");
        }

        String jwt = jwtUtil.generateToken(loginDto.getEmail());

        return new LoginResponseDto(true, jwt);
    }
}
