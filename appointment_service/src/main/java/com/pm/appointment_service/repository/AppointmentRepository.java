package com.pm.appointment_service.repository;

import com.pm.appointment_service.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    Optional<Appointment> findByDoctorIdAndTimeSlot(String doctorId, String timeSlot);
}

