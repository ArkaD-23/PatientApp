package com.pm.appointment_service.dto;

import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.util.AppointmentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {

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

    public AppointmentDto(Appointment appointment, String doctorEmail, String patientEmail) {

        this.id = appointment.getId();
        this.patientId = appointment.getPatientId();
        this.doctorId = appointment.getDoctorId();
        this.timeSlot = appointment.getTimeSlot();
        this.date = appointment.getDate();
        this.status = appointment.getStatus();
        this.doctorName = appointment.getDoctorName();
        this.patientName = appointment.getPatientName();
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
    }
}
