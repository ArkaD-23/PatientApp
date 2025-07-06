package com.pm.auth_service.grpc;

import com.pm.auth_service.dto.BooleanDto;
import com.pm.auth_service.dto.LoginDto;
import com.pm.auth_service.dto.RegisterDto;
import com.pm.auth_service.dto.LoginResponseDto;
import com.pm.auth_service.service.AuthService;
import com.pm.auth_service.grpc.AuthServiceGrpc;
import com.pm.auth_service.grpc.RegisterRequest;
import com.pm.auth_service.grpc.LoginRequest;
import com.pm.auth_service.grpc.BooleanResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;

    public AuthGrpcService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<BooleanResponse> responseObserver) {
        BooleanDto result = authService.registerUser(new RegisterDto(
                request.getEmail(),
                request.getPassword(),
                request.getFullname(),
                request.getRole()
        ));

        BooleanResponse response = BooleanResponse.newBuilder()
                .setStatus(result.getStatus())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<BooleanResponse> responseObserver) {
        LoginResponseDto result = authService.userLogin(new LoginDto(
                request.getEmail(),
                request.getPassword()
        ));

        BooleanResponse response = BooleanResponse.newBuilder()
                .setStatus(result.isStatus())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
