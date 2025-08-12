package com.pm.auth_service.config;

import com.pm.userservice.grpc.UserServiceGrpc;
import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @GrpcClient("user-service")
    private Channel userChannel;

    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userStub() {
        return UserServiceGrpc.newBlockingStub(userChannel);
    }
}

