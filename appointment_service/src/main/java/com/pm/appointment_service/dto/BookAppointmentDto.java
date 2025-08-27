package com.pm.appointment_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAppointmentDto {

    private String patientId;

    private String doctorId;

    private String timeSlot;

    private String date;
}
