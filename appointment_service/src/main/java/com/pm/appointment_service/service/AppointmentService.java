package com.pm.appointment_service.service;

import com.pm.appointment_service.exception.AppointmentAlreadyPresentException;
import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.repository.AppointmentRepository;
import com.pm.appointment_service.util.AppointmentStatus;
import com.pm.userservice.grpc.UserIdRequest;
import com.pm.userservice.grpc.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @GrpcClient("userService")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

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

        UserIdRequest doctorIdRequest = UserIdRequest.newBuilder().setId(doctorId).build();
        UserIdRequest patientIdRequest = UserIdRequest.newBuilder().setId(patientId).build();

        String doctorName = userStub.getUserById(doctorIdRequest).getFullname();
        String patientName = userStub.getUserById(patientIdRequest).getFullname();

        Appointment appointment = new Appointment(patientId, doctorId, timeSlot, date, AppointmentStatus.APPROVED, doctorName, patientName);
        return appointmentRepository.save(appointment);
    }
}

