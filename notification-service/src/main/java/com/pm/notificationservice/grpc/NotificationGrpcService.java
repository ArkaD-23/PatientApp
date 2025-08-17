package com.pm.notificationservice.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class NotificationGrpcService extends NotificationServiceGrpc.NotificationServiceImplBase {

    @Override
    public void sendNotification(NotificationRequest request,
                                 StreamObserver<NotificationResponse> responseObserver) {
        // Fake email sending logic
        System.out.println("📩 Sending email...");
        System.out.println("To Patient " + request.getPatientId() + ": Appointment booked at " + request.getTimeSlot());
        System.out.println("To Doctor " + request.getDoctorId() + ": Appointment booked with patient " + request.getPatientId());

        NotificationResponse response = NotificationResponse.newBuilder()
                .setStatus("SENT")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    // For direct call from Kafka Listener
    public void sendNotification(NotificationRequest request) {
        System.out.println("📩 Sending email from Kafka Event...");
        System.out.println("To Patient " + request.getPatientId() + ": Appointment booked at " + request.getTimeSlot());
        System.out.println("To Doctor " + request.getDoctorId() + ": Appointment booked with patient " + request.getPatientId());
    }
}

