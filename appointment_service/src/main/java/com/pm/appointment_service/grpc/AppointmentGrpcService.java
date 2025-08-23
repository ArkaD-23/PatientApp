package com.pm.appointment_service.grpc;

import com.pm.appointment_service.exception.AppointmentAlreadyPresentException;
import com.pm.appointment_service.model.Appointment;
import com.pm.appointment_service.service.AppointmentService;
import com.pm.appointmentservice.grpc.AppointmentServiceGrpc;
import com.pm.appointmentservice.grpc.BookAppointmentRequest;
import com.pm.appointmentservice.grpc.BookAppointmentResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.kafka.core.KafkaTemplate;

@GrpcService
public class AppointmentGrpcService extends AppointmentServiceGrpc.AppointmentServiceImplBase {

    private final AppointmentService appointmentService;
    private final KafkaTemplate kafkaTemplate;

    public AppointmentGrpcService(AppointmentService appointmentService, KafkaTemplate kafkaTemplate) {
        this.appointmentService = appointmentService;
        this.kafkaTemplate = kafkaTemplate;
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
