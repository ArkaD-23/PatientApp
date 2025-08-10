package com.pm.gateway.config;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Configuration;
import com.pm.user_service.grpc.UserServiceGrpc;
import com.pm.auth_service.grpc.AuthServiceGrpc;

@Configuration
public class GrpcClientConfig {

    @GrpcClient("user-service")
    public UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @GrpcClient("auth-service")
    public AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;
}
