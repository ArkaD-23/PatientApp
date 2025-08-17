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
public class AppointmentController {

    @GrpcClient("appointmentService")
    private AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentServiceStub;

    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(@RequestBody AppointmentDto dto) {

        // Build gRPC request
        BookAppointmentRequest req = BookAppointmentRequest.newBuilder()
                .setPatientId(dto.getPatientId())
                .setDoctorId(dto.getDoctorId())
                .setTimeSlot(dto.getTimeSlot())
                .build();

        // Call gRPC service
        BookAppointmentResponse res = appointmentServiceStub.bookAppointment(req);

        // Convert gRPC response → REST response
        AppointmentResponseDto responseDto = new AppointmentResponseDto(
                res.getStatus(),
                res.getAppointmentId()
        );

        return ResponseEntity.ok(responseDto);
    }
}
