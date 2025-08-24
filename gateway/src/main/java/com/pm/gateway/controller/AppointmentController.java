package com.pm.gateway.controller;

import com.google.protobuf.Empty;
import com.pm.appointmentservice.grpc.*;
import com.pm.gateway.dto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/appointments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppointmentController {

    @GrpcClient("appointmentService")
    private AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentServiceStub;

    @GetMapping("/doctors/{id}")
    public ResponseEntity<List<GetAppointmentsDto>> getDoctorsAppointment(@PathVariable("id") String id) {

        AppointmentUserId req = AppointmentUserId.newBuilder()
                .setUserId(id)
                .build();

        GetAppointmentResponse res = appointmentServiceStub.getDoctorAppointments(req);

        List<GetAppointmentsDto> appointments = res.getAppointmentsList().stream()
                .map(appointment -> new GetAppointmentsDto(
                        appointment.getAppointmentId(),
                        appointment.getDoctorId(),
                        appointment.getPatientId(),
                        appointment.getTimeSlot(),
                        appointment.getDate(),
                        appointment.getStatus(),
                        appointment.getDoctorName(),
                        appointment.getPatientName()
                ))
                .toList();

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<GetAppointmentsDto>> getPatientsAppointment(@PathVariable("id") String id) {

        AppointmentUserId req = AppointmentUserId.newBuilder()
                .setUserId(id)
                .build();

        GetAppointmentResponse res = appointmentServiceStub.getPatientAppointments(req);

        List<GetAppointmentsDto> appointments = res.getAppointmentsList().stream()
                .map(appointment -> new GetAppointmentsDto(
                        appointment.getAppointmentId(),
                        appointment.getDoctorId(),
                        appointment.getPatientId(),
                        appointment.getTimeSlot(),
                        appointment.getDate(),
                        appointment.getStatus(),
                        appointment.getDoctorName(),
                        appointment.getPatientName()
                ))
                .toList();

        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(@RequestBody AppointmentDto dto) {

        BookAppointmentRequest req = BookAppointmentRequest.newBuilder()
                .setPatientId(dto.getPatientId())
                .setDoctorId(dto.getDoctorId())
                .setTimeSlot(dto.getTimeSlot())
                .setDate(dto.getDate())
                .build();

        BookAppointmentResponse res = appointmentServiceStub.bookAppointment(req);

        AppointmentResponseDto responseDto = new AppointmentResponseDto(
                res.getStatus(),
                res.getAppointmentId()
        );

        return ResponseEntity.ok(responseDto);
    }
}
