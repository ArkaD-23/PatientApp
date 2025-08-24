package com.pm.appointment_service.repository;

import com.pm.appointment_service.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    Appointment findByTimeSlotAndDateAndDoctorId(String timeSlot, String date, String doctorId);

    List<Appointment> findByDoctorId(String doctorId);

    List<Appointment> findByPatientId(String patientId);
}

