package com.pm.video_service.grpc;

import com.pm.video_service.redis.SignalingBus;
import com.pm.video_service.service.RoomRegistry;
import com.pm.videoservice.grpc.*;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class VideoGrpcService extends VideoServiceGrpc.VideoServiceImplBase {
    private final RoomRegistry rooms;
    private final SignalingBus bus;

    public VideoGrpcService(RoomRegistry rooms, SignalingBus bus) {
        this.rooms = rooms; this.bus = bus;
    }

    @Override
    public void createRoom(CreateRoomRequest req, io.grpc.stub.StreamObserver<CreateRoomResponse> resp) {
        var room = rooms.create(req.getDoctorId(), req.getPatientId());
        bus.subscribeRoom(room.getRoomId());
        resp.onNext(CreateRoomResponse.newBuilder().setRoomId(room.getRoomId()).build());
        resp.onCompleted();
    }

    @Override
    public void closeRoom(CloseRoomRequest req, io.grpc.stub.StreamObserver<CloseRoomResponse> resp) {
        boolean ok = rooms.close(req.getRoomId());
        resp.onNext(CloseRoomResponse.newBuilder().setOk(ok).build());
        resp.onCompleted();
    }
}
