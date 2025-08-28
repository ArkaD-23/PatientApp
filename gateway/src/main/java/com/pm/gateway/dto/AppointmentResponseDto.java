package com.pm.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {

    private String id;

    private String patientId;

    private String doctorId;

    private String timeSlot;

    private String date;

    private AppointmentStatus status;

    private String doctorName;

    private String patientName;

    private String doctorEmail;

    private String patientEmail;
}
