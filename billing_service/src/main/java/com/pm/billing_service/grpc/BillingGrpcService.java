package com.pm.billing_service.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);
    
    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {
        
        log.info("Received request for creating billing account for user: {}", billingRequest.toString());

        // Business logic to create billing account

        BillingResponse response = BillingResponse.newBuilder()
            .setAccountId("123456")
            .setStatus("ACTIVE")
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
