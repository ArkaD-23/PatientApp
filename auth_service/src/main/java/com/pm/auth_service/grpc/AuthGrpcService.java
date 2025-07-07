package com.pm.auth_service.grpc;

import com.pm.auth_service.dto.*;
import com.pm.auth_service.exception.InvalidTokenException;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.auth_service.exception.UserNotFoundException;
import com.pm.auth_service.service.AuthService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;

    public AuthGrpcService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<BooleanResponse> responseObserver) {

        try{
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
        } catch (UserAlreadyExistsException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }

    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        LoginResponseDto result = authService.userLogin(new LoginDto(
                request.getEmail(),
                request.getPassword()
        ));

        LoginResponse response = LoginResponse.newBuilder()
                .setStatus(result.getStatus())
                .setToken(result.getToken())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void update(UpdateRequest request, StreamObserver<ProfileResponse> responseObserver) {

        try {
            ProfileResponseDto result = authService.userUpdate(
                    new ProfileDto(
                            UUID.fromString(request.getId()),
                            request.getFullname(),
                            request.getEmail(),
                            request.getPassword()
                    ),
                    request.getToken());

            ProfileResponse response = ProfileResponse.newBuilder()
                    .setId(result.getId().toString())
                    .setFullname(result.getFullname())
                    .setUsername(result.getUsername())
                    .setEmail(result.getEmail())
                    .setRole(result.getRole())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (InvalidTokenException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }

    }

    public void getUser(GetUserRequest request, StreamObserver<ProfileResponse> responseObserver) {

        try {
            ProfileResponseDto result = authService.getUser(request.getUsername(), request.getToken());

            ProfileResponse response = ProfileResponse.newBuilder()
                    .setId(result.getId().toString())
                    .setFullname(result.getFullname())
                    .setUsername(result.getUsername())
                    .setEmail(result.getEmail())
                    .setRole(result.getRole())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (InvalidTokenException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    public void deleteUser(DeleteUserRequest request, StreamObserver<BooleanResponse> responseObserver) {

        try {
            BooleanDto result = authService.deleteUser(UUID.fromString(request.getId()), request.getToken());

            BooleanResponse response = BooleanResponse.newBuilder()
                    .setStatus(result.getStatus())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (InvalidTokenException e) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
