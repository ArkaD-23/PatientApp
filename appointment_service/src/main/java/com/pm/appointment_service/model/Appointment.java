package com.pm.appointment_service.model;

import com.pm.appointment_service.util.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    private String id;

    private String patientId;
    private String doctorId;
    private String timeSlot;
    private AppointmentStatus status;

    public Appointment(String patientId, String doctorId, String timeSlot, AppointmentStatus appointmentStatus) {

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeSlot = timeSlot;
        this.status = appointmentStatus;
    }
}

