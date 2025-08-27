package com.pm.appointment_service.controller;

import com.pm.appointment_service.dto.AppointmentDto;
import com.pm.appointment_service.dto.BookAppointmentDto;
import com.pm.appointment_service.exception.AppointmentAlreadyPresentException;
import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AppointmentController(AppointmentService appointmentService, KafkaTemplate<String, String> kafkaTemplate) {
        this.appointmentService = appointmentService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable String doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getAllDoctorAppointments(doctorId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getPatientAppointments(@PathVariable String patientId) {
        try {
            List<Appointment> appointments = appointmentService.getAllPatientAppointments(patientId);
            return ResponseEntity.ok(appointments);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody BookAppointmentDto bookAppointmentDto) {
        try {
            AppointmentDto appointment = appointmentService.bookAppointment(
                    bookAppointmentDto.getPatientId(),
                    bookAppointmentDto.getDoctorId(),
                    bookAppointmentDto.getTimeSlot(),
                    bookAppointmentDto.getDate()
            );

            String event = String.format(
                    "{\"appointmentId\":\"%s\",\"patientId\":\"%s\",\"doctorId\":\"%s\",\"timeSlot\":\"%s\", \"date\":\"%s\",\"doctorName\":\"%s\",\"patientName\":\"%s\",\"doctorEmail\":\"%s\",\"patientEmail\":\"%s\"}",
                    appointment.getId(), bookAppointmentDto.getPatientId(), bookAppointmentDto.getDoctorId(), bookAppointmentDto.getTimeSlot(), bookAppointmentDto.getDate(),
                    appointment.getDoctorName(), appointment.getPatientName(),
                    appointment.getDoctorEmail(), appointment.getPatientEmail()
            );

            kafkaTemplate.send("appointments", appointment.getId(), event);

            return ResponseEntity.ok(appointment);
        } catch (AppointmentAlreadyPresentException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409 Conflict
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error: " + e.getMessage());
        }
    }
}
