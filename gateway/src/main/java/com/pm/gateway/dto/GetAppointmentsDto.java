package com.pm.gateway.dto;

import lombok.*;

import java.util.Date;

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

    private String doctorName;

    private String patientName;

    private Date creationTimeStamp;
}
