package com.pm.user_service.grpc;

import com.pm.user_service.dto.BooleanDto;
import com.pm.user_service.dto.ProfileDto;
import com.pm.user_service.dto.ProfileResponseDto;
import com.pm.user_service.exception.InvalidTokenException;
import com.pm.user_service.exception.UserNotFoundException;
import com.pm.user_service.service.UserService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    public UserGrpcService(UserService userService) {

        this.userService = userService;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        try {
            boolean userCreated = userService.createUser(
                    new ProfileDto(request.getFullname(), request.getEmail(), request.getPassword(), request.getRole())
            );

            CreateUserResponse response = CreateUserResponse.newBuilder()
                    .setSuccess(userCreated)
                    .setMessage(userCreated ? "User created" : "User already exists")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void validateUser(ValidateUserRequest request, StreamObserver<BooleanResponse> responseObserver) {
        try {
            boolean isValid = userService.validateUser(request.getEmail(), request.getPassword());

            BooleanResponse response = BooleanResponse.newBuilder()
                    .setStatus(isValid)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }



    @Override
    public void update(UpdateRequest request, StreamObserver<ProfileResponse> responseObserver) {
        try {
            ProfileResponseDto result = userService.userUpdate(
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
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (InvalidTokenException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<ProfileResponse> responseObserver) {
        try {
            ProfileResponseDto result = userService.getUser(request.getUsername(), request.getToken());

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
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (InvalidTokenException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deleteUser(DeleteUserRequest request, StreamObserver<BooleanResponse> responseObserver) {
        try {
            BooleanDto result = userService.deleteUser(UUID.fromString(request.getId()), request.getToken());

            BooleanResponse response = BooleanResponse.newBuilder()
                    .setStatus(result.getStatus())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (UserNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (InvalidTokenException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
