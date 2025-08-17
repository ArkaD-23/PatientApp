package com.pm.gateway.controller;

import com.pm.gateway.dto.*;
import com.pm.chatservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

    @GrpcClient("chatService")
    private ChatServiceGrpc.ChatServiceBlockingStub chatServiceStub;

    // ✅ Send message
    @PostMapping("/send")
    public ResponseEntity<BooleanDto> sendMessage(@RequestBody SendMessageDto dto) {
        SendMessageRequest req = SendMessageRequest.newBuilder()
                .setSenderId(dto.getSenderId())
                .setReceiverId(dto.getReceiverId())
                .setRoomId(dto.getRoomId())
                .setContent(dto.getContent())
                .build();

        SendMessageResponse res = chatServiceStub.sendMessage(req);

        return ResponseEntity.ok(new BooleanDto(res.getOk()));
    }

    // ✅ Get all messages for a room
    @PostMapping("/messages")
    public ResponseEntity<MessagesResponseDto> getMessages(@RequestBody GetMessagesDto dto) {
        GetMessagesRequest req = GetMessagesRequest.newBuilder()
                .setRoomId(dto.getRoomId())
                .build();

        GetMessagesResponse res = chatServiceStub.getMessages(req);

        // Convert gRPC messages → DTO messages
        List<MessageDto> messages = res.getMessagesList().stream()
                .map(m -> new MessageDto(
                        m.getId(),
                        m.getSenderId(),
                        m.getReceiverId(),
                        m.getRoomId(),
                        m.getContent(),
                        m.getTimestamp()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MessagesResponseDto(messages));
    }
}
