package com.pm.appointment_service.grpc;

import com.pm.appointment_service.exception.AppointmentAlreadyPresentException;
import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.service.AppointmentService;
import com.pm.appointmentservice.grpc.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@GrpcService
public class AppointmentGrpcService extends AppointmentServiceGrpc.AppointmentServiceImplBase {

    private final AppointmentService appointmentService;
    private final KafkaTemplate kafkaTemplate;

    public AppointmentGrpcService(AppointmentService appointmentService, KafkaTemplate kafkaTemplate) {
        this.appointmentService = appointmentService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void getDoctorAppointments(AppointmentUserId request, StreamObserver<GetAppointmentResponse> responseObserver) {

        try {

            List<Appointment> domainAppointments = appointmentService.getAllDoctorAppointments(request.getUserId());

            List<com.pm.appointmentservice.grpc.Appointment> grpcAppointments =
                    domainAppointments.stream()
                            .map(a -> com.pm.appointmentservice.grpc.Appointment.newBuilder()
                                    .setAppointmentId(a.getId())
                                    .setPatientId(a.getPatientId())
                                    .setDoctorId(a.getDoctorId())
                                    .setTimeSlot(a.getTimeSlot())
                                    .setDate(a.getDate())
                                    .setStatus(a.getStatus().toString())
                                    .setDoctorName(a.getDoctorName())
                                    .setPatientName(a.getPatientName())
                                    .build())
                            .toList();

            GetAppointmentResponse response = GetAppointmentResponse.newBuilder()
                    .addAllAppointments(grpcAppointments)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (RuntimeException e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void getPatientAppointments(AppointmentUserId request, StreamObserver<GetAppointmentResponse> responseObserver) {

        try {

            List<Appointment> domainAppointments = appointmentService.getAllPatientAppointments(request.getUserId());

            List<com.pm.appointmentservice.grpc.Appointment> grpcAppointments =
                    domainAppointments.stream()
                            .map(a -> com.pm.appointmentservice.grpc.Appointment.newBuilder()
                                    .setAppointmentId(a.getId())
                                    .setPatientId(a.getPatientId())
                                    .setDoctorId(a.getDoctorId())
                                    .setTimeSlot(a.getTimeSlot())
                                    .setDate(a.getDate())
                                    .setStatus(a.getStatus().toString())
                                    .setDoctorName(a.getDoctorName())
                                    .setPatientName(a.getPatientName())
                                    .build())
                            .toList();

            GetAppointmentResponse response = GetAppointmentResponse.newBuilder()
                    .addAllAppointments(grpcAppointments)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (RuntimeException e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void bookAppointment(BookAppointmentRequest request,
                                StreamObserver<BookAppointmentResponse> responseObserver) {
        try {
            String timeSlot = request.getTimeSlot();
            String date = request.getDate();

            Appointment appointment = appointmentService.bookAppointment(
                    request.getPatientId(),
                    request.getDoctorId(),
                    timeSlot,
                    date
            );

            String event = String.format("{\"appointmentId\":\"%s\",\"patientId\":\"%s\",\"doctorId\":\"%s\",\"timeSlot\":\"%s\"}",
                    appointment.getId(), request.getPatientId(), request.getDoctorId(), request.getTimeSlot());

            kafkaTemplate.send("appointments", appointment.getId(), event);

            BookAppointmentResponse response = BookAppointmentResponse.newBuilder()
                    .setAppointmentId(appointment.getId())
                    .setStatus(appointment.getStatus().toString())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (AppointmentAlreadyPresentException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Internal server error")
                            .augmentDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }
}
