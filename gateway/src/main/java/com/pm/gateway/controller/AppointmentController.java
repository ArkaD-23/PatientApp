package com.pm.gateway.controller;

import com.pm.gateway.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
    public ResponseEntity<List<GetAppointmentsDto>> getDoctorsAppointment(@PathVariable String id) {

        try {
            ResponseEntity<GetAppointmentsDto[]> response = restTemplate.exchange(
                    appointmentServiceUrl + "/doctors/" + id,
                    HttpMethod.GET,
                    null,
                    GetAppointmentsDto[].class
            );
            System.out.println(response.getBody());
            if (response.getBody() != null) {
                return ResponseEntity.ok(new ArrayList<>(Arrays.asList(response.getBody())));
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<List<GetAppointmentsDto>> getPatientsAppointment(@PathVariable("id") String id) {

        try {
            ResponseEntity<GetAppointmentsDto[]> response = restTemplate.exchange(
                    appointmentServiceUrl + "/patients/" + id,
                    HttpMethod.GET,
                    null,
                    GetAppointmentsDto[].class
            );
            System.out.println(response.getBody());
            if (response.getBody() != null) {
                return ResponseEntity.ok(new ArrayList<>(Arrays.asList(response.getBody())));
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/book")
    public ResponseEntity<BooleanDto> bookAppointment(@RequestBody AppointmentDto dto) {

        try {
            HttpEntity<AppointmentDto> request = new HttpEntity<>(dto);
            ResponseEntity<BooleanDto> response = restTemplate.exchange(
                    appointmentServiceUrl + "/book",
                    HttpMethod.POST,
                    request,
                    BooleanDto.class
            );
            System.out.println(response.getBody());
            if (response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
