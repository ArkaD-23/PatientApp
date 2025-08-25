package com.pm.notificationservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.notificationservice.dto.AppointmentEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AppointmentEventListener {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmailService emailService;

    @Value("${notifications.emailDomainFallback:example.com}")
    private String fallbackDomain;

    public AppointmentEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${notifications.topic:appointments}", groupId = "notification-service")
    public void onAppointmentEvent(ConsumerRecord<String, String> record) {
        try {
            String payload = record.value();
            AppointmentEvent evt = objectMapper.readValue(payload, AppointmentEvent.class);

            String patientEmail = (evt.getPatientEmail() != null && !evt.getPatientEmail().isBlank())
                    ? evt.getPatientEmail()
                    : buildFallback(evt.getPatientId());

            String doctorEmail = (evt.getDoctorEmail() != null && !evt.getDoctorEmail().isBlank())
                    ? evt.getDoctorEmail()
                    : buildFallback(evt.getDoctorId());

            String patientSubject = "Your appointment is confirmed";
            String patientBody =
                    "Hello " + safe(evt.getPatientName()) + ",\n\n" +
                            "Your appointment is confirmed.\n" +
                            "Date: " + safe(evt.getDate()) + "\n" +
                            "Time: " + safe(evt.getTimeSlot()) + "\n" +
                            "Doctor: " + safe(evt.getDoctorName()) + "\n\n" +
                            "Appointment ID: " + safe(evt.getAppointmentId()) + "\n\n" +
                            "— MediBridge";

            String doctorSubject = "New appointment booked";
            String doctorBody =
                    "Hello " + safe(evt.getDoctorName()) + ",\n\n" +
                            "A new appointment has been booked.\n" +
                            "Date: " + safe(evt.getDate()) + "\n" +
                            "Time: " + safe(evt.getTimeSlot()) + "\n" +
                            "Patient: " + safe(evt.getPatientName()) + "\n\n" +
                            "Appointment ID: " + safe(evt.getAppointmentId()) + "\n\n" +
                            "— MediBridge";

            emailService.send(patientEmail, patientSubject, patientBody);
            emailService.send(doctorEmail, doctorSubject, doctorBody);

            System.out.printf("✅ Email sent for appointment %s (partition=%d, offset=%d)%n",
                    evt.getAppointmentId(), record.partition(), record.offset());

        } catch (Exception e) {
            System.err.println("❌ Failed to process appointment event: " + e.getMessage());
            e.printStackTrace();
            // In production: send to DLQ / retry, or use @RetryableTopic
        }
    }

    private String buildFallback(String id) {
        return id + "@" + fallbackDomain;
    }

    private String safe(String s) {
        return s == null ? "-" : s;
    }
}


