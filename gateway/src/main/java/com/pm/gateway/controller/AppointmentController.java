package com.pm.gateway.controller;

import com.pm.appointmentservice.grpc.AppointmentServiceGrpc;
import com.pm.appointmentservice.grpc.BookAppointmentRequest;
import com.pm.appointmentservice.grpc.BookAppointmentResponse;
import com.pm.gateway.dto.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/appointments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppointmentController {

    @GrpcClient("appointmentService")
    private AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentServiceStub;

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
