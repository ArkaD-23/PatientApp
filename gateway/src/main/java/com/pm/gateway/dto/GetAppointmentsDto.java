package com.pm.gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAppointmentsDto {

    private String appointmentId;

    private String patientId;

    private String doctorId;

    private String timeSlot;

    private String date;

    private String status;
}
