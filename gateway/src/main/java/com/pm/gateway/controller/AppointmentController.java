package com.pm.gateway.controller;

import com.pm.gateway.dto.AppointmentDto;
import com.pm.gateway.dto.AppointmentResponseDto;
import com.pm.gateway.dto.GetAppointmentsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/appointments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class AppointmentController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${appointment.service.url}")
    private String appointmentServiceUrl;

    @GetMapping("/doctors/{id}")
    public ResponseEntity<List<GetAppointmentsDto>> getDoctorsAppointment(@PathVariable("id") String id) {

        GetAppointmentsDto[] response = restTemplate.getForObject(
                 appointmentServiceUrl + "/doctors/" + id,
                GetAppointmentsDto[].class
        );

        List<GetAppointmentsDto> appointments = Arrays.asList(response);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<GetAppointmentsDto>> getPatientsAppointment(@PathVariable("id") String id) {

        GetAppointmentsDto[] response = restTemplate.getForObject(
                appointmentServiceUrl + "/patients/" + id,
                GetAppointmentsDto[].class
        );

        List<GetAppointmentsDto> appointments = Arrays.asList(response);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(@RequestBody AppointmentDto dto) {

        AppointmentResponseDto responseDto = restTemplate.postForObject(
                appointmentServiceUrl + "/book",
                dto,
                AppointmentResponseDto.class
        );

        return ResponseEntity.ok(responseDto);
    }
}
