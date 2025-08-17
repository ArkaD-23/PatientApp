package com.pm.appointment_service.service;

import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.repository.AppointmentRepository;
import com.pm.appointment_service.util.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment bookAppointment(String patientId, String doctorId, String timeSlot) {
        // check if doctor already has appointment at this timeslot
        Optional<Appointment> existing = appointmentRepository.findByDoctorIdAndTimeSlot(doctorId, timeSlot);
        if (existing.isPresent()) {
            throw new RuntimeException("Doctor not available at this time slot");
        }

        Appointment appointment = new Appointment(patientId, doctorId, timeSlot, AppointmentStatus.APPROVED);
        return appointmentRepository.save(appointment);
    }
}

