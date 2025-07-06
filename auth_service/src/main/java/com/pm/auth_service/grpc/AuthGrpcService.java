package com.pm.auth_service.grpc;

import com.pm.auth_service.dto.BooleanDto;
import com.pm.auth_service.dto.LoginDto;
import com.pm.auth_service.dto.RegisterDto;
import com.pm.auth_service.dto.LoginResponseDto;
import com.pm.auth_service.exception.UserAlreadyExistsException;
import com.pm.auth_service.service.AuthService;
import io.grpc.Status;
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
}
