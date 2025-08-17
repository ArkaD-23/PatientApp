package com.pm.gateway.controller;

import com.pm.gateway.dto.BooleanDto;
import com.pm.gateway.dto.CloseRoomDto;
import com.pm.gateway.dto.CreateRoomRequestDto;
import com.pm.gateway.dto.CreateRoomResponseDto;
import com.pm.videoservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rooms")
public class VideoController {

    @GrpcClient("videoService")
    private VideoServiceGrpc.VideoServiceBlockingStub roomServiceStub;

    @PostMapping("/create")
    public ResponseEntity<CreateRoomResponseDto> createRoom(@RequestBody CreateRoomRequestDto dto) {

        CreateRoomRequest req = CreateRoomRequest.newBuilder()
                .setDoctorId(dto.getDoctorId())
                .setPatientId(dto.getPatientId())
                .build();

        CreateRoomResponse res = roomServiceStub.createRoom(req);

        return ResponseEntity.ok(
                new CreateRoomResponseDto(
                        res.getRoomId()
                )
        );
    }

    @DeleteMapping("/close")
    public ResponseEntity<BooleanDto> closeRoom(@RequestBody CloseRoomDto dto) {
        CloseRoomRequest req = CloseRoomRequest.newBuilder()
                .setRoomId(dto.getRoomId())
                .build();

        CloseRoomResponse res = roomServiceStub.closeRoom(req);

        return ResponseEntity.ok(new BooleanDto(res.getOk()));
    }
}
