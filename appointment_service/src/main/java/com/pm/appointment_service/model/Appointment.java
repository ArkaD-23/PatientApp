package com.pm.appointment_service.model;

import com.pm.appointment_service.util.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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

    private String date;

    private AppointmentStatus status;

    private String doctorName;

    private String patientName;

    @CreatedDate
    private Date creationTimestamp;


    public Appointment(String patientId, String doctorId, String timeSlot, String date, AppointmentStatus appointmentStatus, String doctorName, String patientName) {

        this.patientId = patientId;
        this.doctorId = doctorId;
        this.timeSlot = timeSlot;
        this.date = date;
        this.status = appointmentStatus;
        this.doctorName = doctorName;
        this.patientName = patientName;
    }
}

