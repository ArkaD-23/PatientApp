package com.pm.appointment_service.service;

import com.pm.appointment_service.dto.AppointmentDto;
import com.pm.appointment_service.dto.ProfileResponseDto;
import com.pm.appointment_service.exception.AppointmentAlreadyPresentException;
import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.repository.AppointmentRepository;
import com.pm.appointment_service.util.AppointmentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}")
    private String userServiceUrl;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllDoctorAppointments(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public List<Appointment> getAllPatientAppointments(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public AppointmentDto bookAppointment(String patientId, String doctorId, String timeSlot, String date)
            throws AppointmentAlreadyPresentException {

        Appointment existing = appointmentRepository.findByTimeSlotAndDateAndDoctorId(timeSlot, date, doctorId);
        if (existing != null) {
            throw new AppointmentAlreadyPresentException("Doctor not available at this time slot");
        }

        ResponseEntity<ProfileResponseDto> doctor = restTemplate.exchange(
                userServiceUrl + "/" + doctorId,
                HttpMethod.GET,
                null,
                ProfileResponseDto.class
        );

        ResponseEntity<ProfileResponseDto> patient = restTemplate.exchange(
                userServiceUrl + "/" + patientId,
                HttpMethod.GET,
                null,
                ProfileResponseDto.class
        );

        String doctorName = doctor.getBody() != null ? doctor.getBody().getFullname() : null;
        String patientName = patient.getBody() != null ? patient.getBody().getFullname() : null;
        String doctorEmail = doctor.getBody() != null ? doctor.getBody().getEmail() : null;
        String patientEmail = patient.getBody() != null ? patient.getBody().getEmail() : null;

        Appointment appointment = new Appointment(
                patientId,
                doctorId,
                timeSlot,
                date,
                AppointmentStatus.APPROVED,
                doctorName,
                patientName
        );

        appointmentRepository.save(appointment);

        return new AppointmentDto(appointment, doctorEmail, patientEmail);
    }
}
