package com.pm.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentEvent {

    private String appointmentId;

    private String patientId;

    private String doctorId;

    private String timeSlot;

    private String date;

    private String doctorName;

    private String patientName;

    private String doctorEmail;

    private String patientEmail;
}

