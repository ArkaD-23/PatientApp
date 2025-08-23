package com.pm.user_service.grpc;

import com.google.protobuf.Empty;
import com.pm.user_service.dto.BooleanDto;
import com.pm.user_service.dto.ProfileDto;
import com.pm.user_service.dto.ProfileResponseDto;
import com.pm.user_service.exception.InvalidTokenException;
import com.pm.user_service.exception.UserNotFoundException;
import com.pm.user_service.service.UserService;
import com.pm.userservice.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;

    public UserGrpcService(UserService userService) {

        this.userService = userService;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<BooleanResponse> responseObserver) {
        try {
            boolean userCreated = userService.createUser(
                    new ProfileDto(request.getFullname(), request.getEmail(), request.getPassword(), request.getRole(), request.getUsername())
            );

            BooleanResponse response = BooleanResponse.newBuilder()
                    .setStatus(userCreated)
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
            ProfileResponseDto result = userService.getUser(request.getEmail(), request.getToken());

            ProfileResponse response = ProfileResponse.newBuilder()
                    .setId(result.getId().toString())
                    .setFullname(result.getFullname())
                    .setEmail(result.getEmail())
                    .setRole(result.getRole())
                    .setUsername(result.getUsername())
                    .build();
            System.out.println("getUser in user grpc: " + response.getUsername());
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

//    @Override
//    public void addUser(UserIdRequest request, StreamObserver<BooleanResponse> responseObserver) {
//        try {
//
//            BooleanDto result = userService.saveUser(UUID.fromString(request.getId()));
//
//            BooleanResponse response = BooleanResponse.newBuilder()
//                    .setStatus(result.getStatus())
//                    .build();
//
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//        } catch (UserNotFoundException e) {
//            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
//        }
//    }
//
//    @Override
//    public void disconnectUser(UserIdRequest request, StreamObserver<BooleanResponse> responseObserver) {
//
//        BooleanDto result = userService.disconnect(UUID.fromString(request.getId()));
//
//        BooleanResponse response = BooleanResponse.newBuilder()
//                .setStatus(result.getStatus())
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void getConnectedUsers(Empty request, StreamObserver<ProfileListResponse> responseObserver) {
//
//        List<ProfileResponseDto> result = userService.findConnectedUsers();
//        ProfileListResponse response = ProfileListResponse.newBuilder()
//                .addAllProfiles(
//                        result.stream()
//                                .map(dto -> ProfileResponse.newBuilder()
//                                        .setId(dto.getId().toString())
//                                        .setFullname(dto.getFullname())
//                                        .setEmail(dto.getEmail())
//                                        .setRole(dto.getRole())
//                                        .build()
//                                )
//                                .toList()
//                )
//                .build();
//
//        responseObserver.onNext(response);
//        responseObserver.onCompleted();
//    }

}
