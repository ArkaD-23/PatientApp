package com.pm.appointment_service.service;

import com.pm.appointment_service.exception.AppointmentAlreadyPresentException;
import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.repository.AppointmentRepository;
import com.pm.appointment_service.util.AppointmentStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllDoctorAppointments(String doctorId) {

        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getAllPatientAppointments(String patientId) {

        return appointmentRepository.findByPatientId(patientId);
    }


    public Appointment bookAppointment(String patientId, String doctorId, String timeSlot, String date) throws AppointmentAlreadyPresentException {
        Appointment existing = appointmentRepository.findByTimeSlotAndDateAndDoctorId(timeSlot, date, doctorId);
        if (existing != null) {
            throw new AppointmentAlreadyPresentException("Doctor not available at this time slot");
        }

        Appointment appointment = new Appointment(patientId, doctorId, timeSlot, date, AppointmentStatus.APPROVED);
        return appointmentRepository.save(appointment);
    }
}

