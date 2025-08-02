package com.pm.auth_service.service;

import com.pm.auth_service.dto.*;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.user_service.grpc.*;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserServiceGrpc.UserServiceBlockingStub userStub;
    private final JwtUtil jwtUtil;

    public AuthService(UserServiceGrpc.UserServiceBlockingStub userStub, JwtUtil jwtUtil) {
        this.userStub = userStub;
        this.jwtUtil = jwtUtil;
    }

    public BooleanDto registerUser(RegisterDto registerDto) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setEmail(registerDto.getEmail())
                .setPassword(registerDto.getPassword())
                .setFullname(registerDto.getFullname())
                .setRole(registerDto.getRole())
                .build();

        CreateUserResponse response = userStub.createUser(request);

        if (!response.getSuccess()) {
            throw new UserAlreadyExistsException(response.getMessage());
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
