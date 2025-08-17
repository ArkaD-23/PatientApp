package com.pm.notificationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.notificationservice.grpc.NotificationGrpcService;
import com.pm.notificationservice.grpc.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEventListener {

    private final NotificationGrpcService notificationGrpcService;

    @Autowired
    public AppointmentEventListener(NotificationGrpcService notificationGrpcService) {
        this.notificationGrpcService = notificationGrpcService;
    }

    @KafkaListener(topics = "appointment-success", groupId = "notification-service")
    public void consumeAppointmentEvent(String message) {
        // Example JSON: {"appointmentId":"123","patientId":"p1","doctorId":"d1","timeSlot":"2025-08-17T10:30"}

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(message);

            NotificationRequest request = NotificationRequest.newBuilder()
                    .setAppointmentId(node.get("appointmentId").asText())
                    .setPatientId(node.get("patientId").asText())
                    .setDoctorId(node.get("doctorId").asText())
                    .setTimeSlot(node.get("timeSlot").asText())
                    .build();

            notificationGrpcService.sendNotification(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

